

import java.util.Date;

public class SkillTree {
    private int id;
    private int userId;
    private String name;
    private Date createdAt;
    private Date updatedAt;

    public SkillTree(int id, int userId, String name, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void create() {
        System.out.println("Criaria uma nova skill tree no banco de dados vinculada ao usuário");
    }

    public void update(String newName) {
        System.out.println("Atualizaria o nome da skill tree no banco de dados");
    }

    public void delete() {
        System.out.println("Removeria a skill tree e todos os seus nós do banco de dados");
    }

    public void listNodes() {
        System.out.println("Buscaria e retornaria todos os SkillTreeNodes associados a esta skill tree");
    }

    public void addSubject(int subjectId) {
        System.out.println("Criaria um registro em SkillTreeSubjectRelation vinculando esta skill tree ao subject informado");
    }

    public void removeSubject(int subjectId) {
        System.out.println("Removeria o registro em SkillTreeSubjectRelation que vincula esta skill tree ao subject informado");
    }
}
