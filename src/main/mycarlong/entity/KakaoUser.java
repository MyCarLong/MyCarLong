package java.com.mycarlong.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUser {

    @Id
    private String userid;
    private String pwd;
    private String name;
}
