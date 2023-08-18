package com.example.store.user.domain.entity;

import com.example.store.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // id

    @Column(unique = true)
    private String userPhoneNum;// 핸드폰 번호
    private String userPassword;// 비밀번호

    // private List<String>roles;
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();


    // Spring Security에서 사용자의 권한을 관리하기 위한 메서드입니다.
    /*
    사용자 권한 관리: 사용자가 어떤 기능에 접근 가능한지 결정
    인증과 권한 검사: 해당 사용자의 권한을 검사하여 접근이 허용되는지 여부를 판단
    메뉴 또는 기능 제한: 해당 권한을 가진 사용자만이 접근할 수 있는 기능을 구현
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 역할(Role) 정보를 갖고 있는 roles 리스트를 스트림으로 변환하여 처리합니다.
        return this.roles.stream()
                // 각 역할 정보를 SimpleGrantedAuthority 클래스의 생성자를 통해 GrantedAuthority 객체로 변환합니다.
                .map(SimpleGrantedAuthority::new)
                // 변환된 권한 객체들을 리스트로 수집(collect)하여 반환합니다.
                .collect(Collectors.toList());
    }

    // 여기부터<<

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String getPassword() {
        return userPassword; // 비밀번호
    }

    @Override
    public String getUsername() {
        return userPhoneNum; // 휴대폰 번호
    }

    // 여기까지>>

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호) 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }
}
