import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.example.configuration.ConsulConfig;

@ApplicationPath("/")
public class PersonApplication extends Application {

    @PostConstruct
    public void init() {

        ConsulConfig.registerService();
    }
}
