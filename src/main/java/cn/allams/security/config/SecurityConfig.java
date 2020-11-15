package cn.allams.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService myUserDetailsService;

    // 注入数据源
    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 创建保存记住我信息的表，用一次就够了，第二次会报错
        // jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(password());
    }
    @Bean
    PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }
    // 其他配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 退出
        http.logout().logoutUrl("/logout").
                logoutSuccessUrl("/index.html").permitAll();
        // 配置没有权限跳转的自定义页面
        http.exceptionHandling().accessDeniedPage("/noauth.html");
        http.formLogin() //自定义自己编写的登陆页面
            .loginPage("/login.html") //登录页面设置
            .loginProcessingUrl("/user/login") //登录界面的url
            .defaultSuccessUrl("/success.html").permitAll() //登陆成功后默认跳转路径
            .and().authorizeRequests()
                .antMatchers("/", "/user/login", "/test/hello").permitAll() //设置不需要认证的路径
                // 当前登录用户，只有具有admins权限的才可以访问这个路径
                //.antMatchers("/test/admin").hasAuthority("admins")
                // 同时设置多个权限
                .antMatchers("/test/manager").hasAnyAuthority("admins","manager")
            .anyRequest().authenticated()
                //记住我
                .and().rememberMe().tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60) //设置有效时长
                .userDetailsService(userDetailsService())
                .and().csrf().disable(); //关闭csrf
    }

    // 配置账号密码方式
    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 加密密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("123456");
        // 配置用户名密码和角色
        auth.inMemoryAuthentication().withUser("allams").password(password).roles("admin");
    }
    */
}
