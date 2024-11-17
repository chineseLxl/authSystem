import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SignInUser {
    // 密码加密
    @Test
    public void Sign() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String psw = "admin";
        String encode = bCryptPasswordEncoder.encode(psw);
        System.out.printf("password:%s,encode:%s\n",psw,encode);
        System.out.println(bCryptPasswordEncoder.matches(psw,encode));
    }
}
