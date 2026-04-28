

import java.util.Date;

public class SkillTreeNode {
    private int id;
    private String name;
    private String description;
    private int skillTreeId;
    private Integer fatherNodeId;
    private boolean isRoot;
    private Date createdAt;
    private Date updatedAt;

    public SkillTreeNode(int id, String name, String description, int skillTreeId, Integer fatherNodeId, boolean isRoot, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.skillTreeId = skillTreeId;
        this.fatherNodeId = fatherNodeId;
        this.isRoot = isRoot;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getSkillTreeId() { return skillTreeId; }
    public Integer getFatherNodeId() { return fatherNodeId; }
    public boolean isRoot() { return isRoot; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void create() {
        System.out.println("Criaria um novo nó no banco de dados vinculado à skill tree e ao nó pai informados");
    }

    public void update(String newName, String newDescription) {
        System.out.println("Atualizaria o nome e a descrição do nó no banco de dados");
    }

    public void delete() {
        System.out.println("Removeria o nó e todos os seus filhos, questões e vídeos associados");
    }

    public void listChildren() {
        System.out.println("Buscaria todos os nós cujo fatherNodeId aponta para este nó");
    }

    public void listQuestions() {
        System.out.println("Buscaria todas as Questions vinculadas a este nó pelo skillTreeNodeId");
    }

    public void listVideos() {
        System.out.println("Buscaria todos os Videos vinculados a este nó pelo skillTreeNodeId");
    }
}
