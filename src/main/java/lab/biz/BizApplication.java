package lab.biz;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

//@ServletComponentScan //서블릿 자동등록
@Slf4j
@SpringBootApplication
public class BizApplication {

	public static void main(String[] args) {
		SpringApplication.run(BizApplication.class, args);
//		ConfigurableApplicationContext ac =  SpringApplication.run(BizApplication.class, args);
//		String[] allBeans = ac.getBeanDefinitionNames();
//		for (String bean : allBeans) {
//			log.info("bean : " + bean);
	}

}
