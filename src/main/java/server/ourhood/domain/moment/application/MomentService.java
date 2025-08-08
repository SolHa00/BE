package server.ourhood.domain.moment.application;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.comment.dao.CommentRepository;
import server.ourhood.domain.comment.domain.Comment;
import server.ourhood.domain.comment.dto.response.CommentInfoResponse;
import server.ourhood.domain.image.application.ImageService;
import server.ourhood.domain.image.domain.Image;
import server.ourhood.domain.moment.dao.MomentRepository;
import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.moment.dto.request.MomentCreateRequest;
import server.ourhood.domain.moment.dto.request.MomentUpdateRequest;
import server.ourhood.domain.moment.dto.response.GetMomentResponse;
import server.ourhood.domain.moment.dto.response.GetMomentResponse.MomentDetail;
import server.ourhood.domain.moment.dto.response.GetMomentResponse.MomentMetadata;
import server.ourhood.domain.moment.dto.response.MomentCreateResponse;
import server.ourhood.domain.room.application.RoomService;
import server.ourhood.domain.room.dao.RoomRepository;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.util.CloudFrontUtil;

@Service
@RequiredArgsConstructor
public class MomentService {

	private final MomentRepository momentRepository;
	private final RoomService roomService;
	private final ImageService imageService;
	private final CloudFrontUtil cloudFrontUtil;
	private final CommentRepository commentRepository;
	private final RoomRepository roomRepository;
	private static final Long ROOT_COMMENT_PARENT_ID = -1L;

	public Moment getByMomentId(Long momentId) {
		return momentRepository.findById(momentId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MOMENT));
	}

	@Transactional
	public MomentCreateResponse createMoment(User user, MomentCreateRequest request) {
		Room room = roomService.getByRoomId(request.roomId());
		validateRoomMember(room.getId(), user);
		Image image = imageService.findImageByKey(request.momentImageKey());
		imageService.activateAndMoveImage(image);
		Moment moment = Moment.createMoment(image, request.momentDescription(), room, user);
		momentRepository.save(moment);
		return new MomentCreateResponse(moment.getId());
	}

	private void validateRoomMember(Long roomId, User user) {
		if (!roomRepository.existsByIdAndRoomMembersUser(roomId, user)) {
			throw new BaseException(NOT_ROOM_MEMBER);
		}
	}

	@Transactional
	public void updateMoment(User user, Long momentId, MomentUpdateRequest request) {
		Moment moment = getByMomentId(momentId);
		moment.validateOwner(user);
		moment.updateDescription(request.momentDescription());
	}

	@Transactional
	public void deleteMoment(User user, Long momentId) {
		Moment moment = getByMomentId(momentId);
		moment.validateOwner(user);
		imageService.deleteImage(moment.getImage());
		momentRepository.delete(moment);
	}

	@Transactional(readOnly = true)
	public GetMomentResponse getMoment(User user, Long momentId) {
		Moment moment = momentRepository.findByIdWithOwnerAndImage(momentId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MOMENT));
		validateRoomMember(moment.getRoom().getId(), user);
		List<Comment> allComments = commentRepository.findAllCommentsByMomentId(momentId);
		Map<Long, List<Comment>> commentsByParentId = groupCommentsByParentId(allComments);
		List<CommentInfoResponse> commentInfoResponse = convertToCommentInfoResponse(commentsByParentId);

		MomentMetadata momentMetadata = MomentMetadata.of(
			cloudFrontUtil.getImageUrl(moment.getImage().getPermanentFileName()),
			moment);
		MomentDetail momentDetail = MomentDetail.of(moment.getDescription());
		return new GetMomentResponse(momentMetadata, momentDetail, commentInfoResponse);
	}

	/**
	 * 전체 댓글 리스트를 부모 댓글 ID를 기준으로 그룹핑
	 */
	private Map<Long, List<Comment>> groupCommentsByParentId(List<Comment> allComments) {
		return allComments.stream()
			.collect(
				Collectors.groupingBy(
					comment -> {
						Comment parent = comment.getParent();
						return (parent != null)
							? parent.getId()
							: ROOT_COMMENT_PARENT_ID;
					}));
	}

	/**
	 * 그룹핑된 댓글 Map을 계층적인 DTO 리스트로 변환
	 */
	private List<CommentInfoResponse> convertToCommentInfoResponse(Map<Long, List<Comment>> commentsByParentId) {
		List<Comment> rootComments = commentsByParentId.getOrDefault(ROOT_COMMENT_PARENT_ID, List.of());
		return rootComments.stream()
			.map(comment -> buildCommentInfoResponse(comment, commentsByParentId))
			.collect(Collectors.toList());
	}

	/**
	 * 댓글 엔티티 하나를 DTO로 변환하는 재귀 헬퍼 메서드
	 */
	private CommentInfoResponse buildCommentInfoResponse(Comment comment, Map<Long, List<Comment>> commentsByParentId) {
		List<CommentInfoResponse> replyCommentsResponses = commentsByParentId.getOrDefault(comment.getId(), List.of())
			.stream()
			.map(childComment -> buildCommentInfoResponse(childComment, commentsByParentId))
			.collect(Collectors.toList());

		return CommentInfoResponse.of(
			(comment.getParent() != null) ? comment.getParent().getId() : null,
			comment.getId(),
			comment.getContent(),
			comment.getOwner().getId(),
			comment.getOwner().getNickname(),
			comment.getCreatedAt(),
			replyCommentsResponses);
	}
}
