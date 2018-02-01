package uk.gov.digital.ho.egar.vscan.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger1.annotations.EnableSwagger;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import uk.gov.digital.ho.egar.constants.SwaggerConstants;

import java.time.LocalDate;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Configure Swagger 2 for use with this project.
 * 
 * @author Bruce.Mundin
 * @see https://dzone.com/articles/spring-boot-swagger-ui
 */
@Configuration
@EnableSwagger  //Enable swagger 1.2 spec
@EnableSwagger2 //Enable swagger 2.0 spec
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    @Bean
    public Docket api() {                
        return new Docket(DocumentationType.SWAGGER_2)          
          .select()                                       
          .paths(apiPaths())
          .build()
          .directModelSubstitute(LocalDate.class, String.class) //Substitutes all the LocalDate objects in the JSON POJOS with a String
          .apiInfo(apiInfo());
    }

    private Predicate<String> apiPaths() {
        return regex(SwaggerConstants.PATH_DATA + "/.*" );
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(  "VScan API",                       // title
                                         "API for Virus scan",                  // description
                                         "4.0.1",                                 // version
                                        "Civica API terms of service",      // termsOfServiceUrl
                                        new Contact("",
                                                    "",
                                                    ""),     // contact
                                        "API Licence",                          // license
                                        "/licence.html");                       // licenseUrl
        

        
        return apiInfo;
    }
}