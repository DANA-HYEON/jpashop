package jpabook.jpashop.domain;


import jakarta.persistence.Embeddable;
import lombok.Getter;

//JPA 내장타입
@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    //jpa의 proxy등의 기술을 쓰려면 기본 생성자가 필요
    //하지만 기본생성자를 public으로 열어놓으면 다른 사용자가 호출할 수 있기 때문에
    //protected 수정
    //즉, jpa 스펙상 필요한 기본 생성자
    protected Address() {}

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
