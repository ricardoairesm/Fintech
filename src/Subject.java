

import java.util.Date;

public class Subject {
    private int id;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;

    public Subject(int id, String name, String description, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void create() {
        System.out.println("Salvaria o novo subject no banco de dados");
    }

    public void update(String newName, String newDescription) {
        System.out.println("Atualizaria o nome e a descrição do subject no banco de dados");
    }

    public void delete() {
        System.out.println("Removeria o subject e seus relacionamentos com skill trees do banco de dados");
    }

    public void listSkillTrees() {
        System.out.println("Buscaria todas as SkillTrees associadas a este subject via SkillTreeSubjectRelation");
    }
}
