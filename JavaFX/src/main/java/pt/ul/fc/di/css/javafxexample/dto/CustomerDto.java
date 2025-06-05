package pt.ul.fc.di.css.javafxexample.dto;
public class CustomerDto {
    private Long id;
    private String vatNumber;
    private String designation;
    private String phoneNumber;

    public CustomerDto() {
    }

    public CustomerDto(Long id, String vatNumber, String designation, String phoneNumber) {
        this.id = id;
        this.vatNumber = vatNumber;
        this.designation = designation;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
