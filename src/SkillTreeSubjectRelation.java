

public class SkillTreeSubjectRelation {
    private int subjectId;
    private int skillTreeId;

    public SkillTreeSubjectRelation(int subjectId, int skillTreeId) {
        this.subjectId = subjectId;
        this.skillTreeId = skillTreeId;
    }

    public int getSubjectId() { return subjectId; }
    public int getSkillTreeId() { return skillTreeId; }

    public void associate() {
        System.out.println("Criaria o registro de relacionamento entre o subject e a skill tree no banco de dados");
    }

    public void dissociate() {
        System.out.println("Removeria o registro de relacionamento entre o subject e a skill tree do banco de dados");
    }

    public void exists() {
        System.out.println("Verificaria se já existe um relacionamento entre o subject e a skill tree informados");
    }
}
