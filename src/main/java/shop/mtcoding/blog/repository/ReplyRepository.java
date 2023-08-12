package shop.mtcoding.blog.repository;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.Reply;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ReplyRepository {

    @Autowired
    private EntityManager em;


    @Transactional
    public void save(ReplyWriteDTO replyWriteDTO,Integer userId){
        Query query = em.createNativeQuery("insert into reply_tb (comment , board_id,user_id) values(:comment,:boardId,:userId)");
        query.setParameter("comment", replyWriteDTO.getComment());
        query.setParameter("boardId",replyWriteDTO.getBoardId());
        query.setParameter("userId",userId);
        query.executeUpdate();
    }

    public List<Reply> findByBoardId(Integer boardId){
        Query query = em.createNativeQuery("SELECT bt.id board_id, bt.content board_content, bt.title board_title, bt.user_id board_user_id, rt.id reply_id,rt.comment reply_comment, rt.board_id reply_board_id, rt.user_id reply_user_id, ut.username username FROM  BOARD_TB bt left outer join reply_tb rt on :boardId = rt.board_id left outer join user_tb ut on rt.user_id = ut.id", Reply.class);
        query.setParameter("boardId", boardId);
        return query.getResultList();
    }
    @Transactional
    public void deleteById(Integer replyId){
        Query query = em.createNativeQuery("delete from reply_tb where id = :replyId");
        query.setParameter("replyId",replyId);
        query.executeUpdate();
    }
}
