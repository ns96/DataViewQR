/**
 * This class contains generated code from the Codename One Designer, DO NOT MODIFY!
 * This class is designed for subclassing that way the code generator can overwrite it
 * anytime without erasing your changes which should exist in a subclass!
 * For details about this file and how it works please read this blog post:
 * http://codenameone.blogspot.com/2010/10/ui-builder-class-how-to-actually-use.html
*/
package generated;

import com.codename1.ui.*;
import com.codename1.ui.util.*;
import com.codename1.ui.plaf.*;
import java.util.Hashtable;
import com.codename1.ui.events.*;

public abstract class StateMachineBase extends UIBuilder {
    private Container aboutToShowThisContainer;
    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    /**
    * @deprecated use the version that accepts a resource as an argument instead
    
**/
    protected void initVars() {}

    protected void initVars(Resources res) {}

    public StateMachineBase(Resources res, String resPath, boolean loadTheme) {
        startApp(res, resPath, loadTheme);
    }

    public Container startApp(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("CheckBox", com.codename1.ui.CheckBox.class);
        UIBuilder.registerCustomComponent("MultiList", com.codename1.ui.list.MultiList.class);
        UIBuilder.registerCustomComponent("Dialog", com.codename1.ui.Dialog.class);
        if(loadTheme) {
            if(res == null) {
                try {
                    if(resPath.endsWith(".res")) {
                        res = Resources.open(resPath);
                        System.out.println("Warning: you should construct the state machine without the .res extension to allow theme overlays");
                    } else {
                        res = Resources.openLayered(resPath);
                    }
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        if(res != null) {
            setResourceFilePath(resPath);
            setResourceFile(res);
            initVars(res);
            return showForm(getFirstFormName(), null);
        } else {
            Form f = (Form)createContainer(resPath, getFirstFormName());
            initVars(fetchResourceFile());
            beforeShow(f);
            f.show();
            postShow(f);
            return f;
        }
    }

    protected String getFirstFormName() {
        return "Main";
    }

    public Container createWidget(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("CheckBox", com.codename1.ui.CheckBox.class);
        UIBuilder.registerCustomComponent("MultiList", com.codename1.ui.list.MultiList.class);
        UIBuilder.registerCustomComponent("Dialog", com.codename1.ui.Dialog.class);
        if(loadTheme) {
            if(res == null) {
                try {
                    res = Resources.openLayered(resPath);
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        return createContainer(resPath, "Main");
    }

    protected void initTheme(Resources res) {
            String[] themes = res.getThemeResourceNames();
            if(themes != null && themes.length > 0) {
                UIManager.getInstance().setThemeProps(res.getTheme(themes[0]));
            }
    }

    public StateMachineBase() {
    }

    public StateMachineBase(String resPath) {
        this(null, resPath, true);
    }

    public StateMachineBase(Resources res) {
        this(res, null, true);
    }

    public StateMachineBase(String resPath, boolean loadTheme) {
        this(null, resPath, loadTheme);
    }

    public StateMachineBase(Resources res, boolean loadTheme) {
        this(res, null, loadTheme);
    }

    public com.codename1.ui.list.MultiList findHistoryMultiList(Component root) {
        return (com.codename1.ui.list.MultiList)findByName("historyMultiList", root);
    }

    public com.codename1.ui.list.MultiList findHistoryMultiList() {
        com.codename1.ui.list.MultiList cmp = (com.codename1.ui.list.MultiList)findByName("historyMultiList", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.list.MultiList)findByName("historyMultiList", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.list.MultiList findMultiList(Component root) {
        return (com.codename1.ui.list.MultiList)findByName("MultiList", root);
    }

    public com.codename1.ui.list.MultiList findMultiList() {
        com.codename1.ui.list.MultiList cmp = (com.codename1.ui.list.MultiList)findByName("MultiList", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.list.MultiList)findByName("MultiList", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.CheckBox findDemoCheckBox(Component root) {
        return (com.codename1.ui.CheckBox)findByName("demoCheckBox", root);
    }

    public com.codename1.ui.CheckBox findDemoCheckBox() {
        com.codename1.ui.CheckBox cmp = (com.codename1.ui.CheckBox)findByName("demoCheckBox", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.CheckBox)findByName("demoCheckBox", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findOkButton(Component root) {
        return (com.codename1.ui.Button)findByName("okButton", root);
    }

    public com.codename1.ui.Button findOkButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("okButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("okButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public static final int COMMAND_SetupDialogOK = 3;

    protected boolean onSetupDialogOK() {
        return false;
    }

    protected void processCommand(ActionEvent ev, Command cmd) {
        switch(cmd.getId()) {
            case COMMAND_SetupDialogOK:
                if(onSetupDialogOK()) {
                    ev.consume();
                    return;
                }
                break;

        }
        if(ev.getComponent() != null) {
            handleComponentAction(ev.getComponent(), ev);
        }
    }

    protected void exitForm(Form f) {
        if("SetupDialog".equals(f.getName())) {
            exitSetupDialog(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(f.getName())) {
            exitMain(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("HistoryForm".equals(f.getName())) {
            exitHistoryForm(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void exitSetupDialog(Form f) {
    }


    protected void exitMain(Form f) {
    }


    protected void exitHistoryForm(Form f) {
    }

    protected void beforeShow(Form f) {
    aboutToShowThisContainer = f;
        if("SetupDialog".equals(f.getName())) {
            beforeSetupDialog(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(f.getName())) {
            beforeMain(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("HistoryForm".equals(f.getName())) {
            beforeHistoryForm(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void beforeSetupDialog(Form f) {
    }


    protected void beforeMain(Form f) {
    }


    protected void beforeHistoryForm(Form f) {
    }

    protected void beforeShowContainer(Container c) {
        aboutToShowThisContainer = c;
        if("SetupDialog".equals(c.getName())) {
            beforeContainerSetupDialog(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(c.getName())) {
            beforeContainerMain(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("HistoryForm".equals(c.getName())) {
            beforeContainerHistoryForm(c);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void beforeContainerSetupDialog(Container c) {
    }


    protected void beforeContainerMain(Container c) {
    }


    protected void beforeContainerHistoryForm(Container c) {
    }

    protected void postShow(Form f) {
        if("SetupDialog".equals(f.getName())) {
            postSetupDialog(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(f.getName())) {
            postMain(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("HistoryForm".equals(f.getName())) {
            postHistoryForm(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void postSetupDialog(Form f) {
    }


    protected void postMain(Form f) {
    }


    protected void postHistoryForm(Form f) {
    }

    protected void postShowContainer(Container c) {
        if("SetupDialog".equals(c.getName())) {
            postContainerSetupDialog(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(c.getName())) {
            postContainerMain(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("HistoryForm".equals(c.getName())) {
            postContainerHistoryForm(c);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void postContainerSetupDialog(Container c) {
    }


    protected void postContainerMain(Container c) {
    }


    protected void postContainerHistoryForm(Container c) {
    }

    protected void onCreateRoot(String rootName) {
        if("SetupDialog".equals(rootName)) {
            onCreateSetupDialog();
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(rootName)) {
            onCreateMain();
            aboutToShowThisContainer = null;
            return;
        }

        if("HistoryForm".equals(rootName)) {
            onCreateHistoryForm();
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void onCreateSetupDialog() {
    }


    protected void onCreateMain() {
    }


    protected void onCreateHistoryForm() {
    }

    protected Hashtable getFormState(Form f) {
        Hashtable h = super.getFormState(f);
        if("SetupDialog".equals(f.getName())) {
            getStateSetupDialog(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Main".equals(f.getName())) {
            getStateMain(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("HistoryForm".equals(f.getName())) {
            getStateHistoryForm(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

            return h;
    }


    protected void getStateSetupDialog(Form f, Hashtable h) {
    }


    protected void getStateMain(Form f, Hashtable h) {
    }


    protected void getStateHistoryForm(Form f, Hashtable h) {
    }

    protected void setFormState(Form f, Hashtable state) {
        super.setFormState(f, state);
        if("SetupDialog".equals(f.getName())) {
            setStateSetupDialog(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(f.getName())) {
            setStateMain(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("HistoryForm".equals(f.getName())) {
            setStateHistoryForm(f, state);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void setStateSetupDialog(Form f, Hashtable state) {
    }


    protected void setStateMain(Form f, Hashtable state) {
    }


    protected void setStateHistoryForm(Form f, Hashtable state) {
    }

    protected boolean setListModel(List cmp) {
        String listName = cmp.getName();
        if("historyMultiList".equals(listName)) {
            return initListModelHistoryMultiList(cmp);
        }
        if("MultiList".equals(listName)) {
            return initListModelMultiList(cmp);
        }
        return super.setListModel(cmp);
    }

    protected boolean initListModelHistoryMultiList(List cmp) {
        return false;
    }

    protected boolean initListModelMultiList(List cmp) {
        return false;
    }

    protected void handleComponentAction(Component c, ActionEvent event) {
        Container rootContainerAncestor = getRootAncestor(c);
        if(rootContainerAncestor == null) return;
        String rootContainerName = rootContainerAncestor.getName();
        Container leadParentContainer = c.getParent().getLeadParent();
        if(leadParentContainer != null && leadParentContainer.getClass() != Container.class) {
            c = c.getParent().getLeadParent();
        }
        if(rootContainerName == null) return;
        if(rootContainerName.equals("SetupDialog")) {
            if("demoCheckBox".equals(c.getName())) {
                onSetupDialog_DemoCheckBoxAction(c, event);
                return;
            }
            if("okButton".equals(c.getName())) {
                onSetupDialog_OkButtonAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Main")) {
            if("MultiList".equals(c.getName())) {
                onMain_MultiListAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("HistoryForm")) {
            if("historyMultiList".equals(c.getName())) {
                onHistoryForm_HistoryMultiListAction(c, event);
                return;
            }
        }
    }

      protected void onSetupDialog_DemoCheckBoxAction(Component c, ActionEvent event) {
      }

      protected void onSetupDialog_OkButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_MultiListAction(Component c, ActionEvent event) {
      }

      protected void onHistoryForm_HistoryMultiListAction(Component c, ActionEvent event) {
      }

}
