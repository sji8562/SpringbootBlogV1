package shop.mtcoding.blog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

//User(1) - Reply(N)
//Board(1) - Reply(N)
@Getter
@Setter
@Table(name = "reply_tb")
@Entity
public class Reply {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(nullable = false, length = 100)
    private String comment;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user; //FK


    @ManyToOne
    private Board board;
}
