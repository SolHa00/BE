package server.ourhood.domain.comment.dto.request;

public record CommentCreateRequest(
	Long momentId,
	String commentContent,
	Long parentId
) {
}
