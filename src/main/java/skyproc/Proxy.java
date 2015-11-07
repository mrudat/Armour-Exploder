package skyproc;

import skyproc.BodyTemplate.BodyTemplateType;

public class Proxy {

    public static Mod getSrcMod(MajorRecord<?> foo) {
        return foo.srcMod;
    }

    public static BodyTemplateType getBodyTemplateType(BodyTemplate foo) {
        for (BodyTemplateType type : BodyTemplateType.values()) {
            if (foo.subRecords.containsStrict(type.type)) {
                return type;
            }
        }
        return BodyTemplateType.Biped;
    }

    private static boolean isBodyTemplateValid(BodyTemplate foo) {
        int count = 0;
        for (BodyTemplateType type : BodyTemplateType.values()) {
            if (foo.subRecords.containsStrict(type.type)) {
                count++;
            }
        }
        if (count > 1) {
            return false;
        }
        return true;
    }
}
