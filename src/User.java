

import java.util.Date;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String celphone;
    private Date createdAt;
    private Date updatedAt;

    public User(int id, String username, String password, String email, String celphone, Date createdAt, Date updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.celphone = celphone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getCelphone() { return celphone; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void register() {
        System.out.println("Salvaria o novo usuário no banco de dados com username e email únicos");
    }

    public void login(String username, String password) {
        System.out.println("Verificaria as credenciais do usuário e retornaria um token de autenticação");
    }

    public void updateProfile(String email, String celphone) {
        System.out.println("Atualizaria os dados de perfil do usuário no banco de dados");
    }

    public void changePassword(String currentPassword, String newPassword) {
        System.out.println("Validaria a senha atual e salvaria a nova senha criptografada");
    }

    public void deleteAccount() {
        System.out.println("Removeria o usuário e todos os seus dados associados do banco de dados");
    }
}
