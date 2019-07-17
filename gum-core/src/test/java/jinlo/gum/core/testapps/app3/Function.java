package jinlo.gum.core.testapps.app3;

import jinlo.gum.core.reduce.Reducers;
import jinlo.gum.core.runtime.BusinessProcesses;
import jinlo.gum.core.runtime.ExtensionExecuteDetail;

public class Function {

    public int getNumber() {
        Entity entity = new Entity();
        int num = BusinessProcesses.executeExtension(entity, Ext1.class, Reducers.first(), p ->
                p.getNum() + 1
        );
        return num;
    }

    public Integer getNumberGreaterThan150(){
        Entity entity = new Entity();
        Integer num= BusinessProcesses.executeExtension(entity, Ext1.class, Reducers.firstOf(p->p>150), p ->
                p.getNum()
        );
        return num;
    }

    public ExtensionExecuteDetail<Integer,Integer> getNumberGreaterThan150WithDetail(){
        Entity entity = new Entity();
        ExtensionExecuteDetail<Integer,Integer> detail= BusinessProcesses.executeExtensionWithDetail(entity, Ext1.class, Reducers.firstOf(p->p>150), p ->
                p.getNum()
        );
        return detail;
    }

    public Integer getNumberGreaterThan300(){
        Entity entity = new Entity();
        Integer num= BusinessProcesses.executeExtension(entity, Ext1.class, Reducers.firstOf(p->p>300), p ->
                p.getNum()
        );
        return num;
    }
}
