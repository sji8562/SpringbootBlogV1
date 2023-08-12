package shop.mtcoding.blog.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.UserUpdateDTO;
import shop.mtcoding.blog.model.User;

// BoardController, UserController, UserRepository
// EntityManager, HttpSession
@Repository
public class UserRepository {

    @Autowired
    private EntityManager em;

    public User findByUsername(String username) {
        Query query = em.createNativeQuery("select * from user_tb where username=:username",User.class);
        query.setParameter("username", username);
        return (User) query.getSingleResult();
    }

    public User findById(Integer Id) {
        Query query = em.createNativeQuery("select * from user_tb where id = :id", User.class);
        query.setParameter("id", Id);
        return (User) query.getSingleResult();
    }

    @Transactional
    public void save(JoinDTO joinDTO) {

        Query query = em.createNativeQuery("insert into user_tb(username, password, email) values(:username, :password, :email)");
        query.setParameter("username", joinDTO.getUsername());
        query.setParameter("password", joinDTO.getPassword());
        query.setParameter("email", joinDTO.getEmail());
        query.executeUpdate();
    }

    @Transactional
    public void updateByPassword(UserUpdateDTO userUpdateDTO){
        Query query = em.createNativeQuery("update user_tb set password = :password , email = :email where username = :username");
        query.setParameter("password",userUpdateDTO.getPassword());
        query.setParameter("email",userUpdateDTO.getEmail());
        query.setParameter("username",userUpdateDTO.getUsername());
        query.executeUpdate();
    }
}
