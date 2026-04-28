import java.util.Date;

public abstract class Content {

    private int id;
    private String name;
    private String description;
    private int skillTreeNodeId;
    private Date createdAt;
    private Date updatedAt;

    public Content(int id, String name, String description, int skillTreeNodeId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.skillTreeNodeId = skillTreeNodeId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getSkillTreeNodeId() { return skillTreeNodeId; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void create() {
        System.out.println("Salvaria o conteúdo '" + name + "' no banco de dados.");
    }

    public void delete() {
        System.out.println("Removeria o conteúdo '" + name + "' do banco de dados.");
    }

    public abstract void display();
}