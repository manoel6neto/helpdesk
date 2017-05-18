package com.mycompany.maven.jsf;

import com.mycompany.maven.ejb.RoleBean;
import com.mycompany.maven.ejb.UserBean;
import com.mycompany.maven.model.Role;
import com.mycompany.maven.model.User;
import com.mycompany.maven.model.comparators.RoleComparator;
import com.mycompany.maven.servlet.ChangePasswordFilter;
import com.mycompany.maven.util.Criptografia;
import com.mycompany.maven.util.JsfUtil;
import com.mycompany.maven.util.Mail;
import com.mycompany.maven.util.PasswordGenerator;
import com.mycompany.maven.util.Utils;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.glassfish.jersey.internal.util.Base64;

@Named
@ViewScoped
public class UserController extends BaseController {

    @Inject
    private MailController mailController;

    @EJB
    private UserBean userBean;
    private User user;
    private String oldPass;
    private String email;
    private String changeEmail;
    private String newPass;
    private List<User> users;

    @EJB
    private RoleBean roleBean;
    private Role systemRole;
    private Integer selectedRoleId;
    private List<Role> roleList;

    @PostConstruct
    public void init() {

        //Limpando o cache das consultas
        userBean.clearCache();

        //Carregando o usuario para novos usuários ou edições
        User requestUser = (User) getFlash("user");
        if (requestUser != null) {
            user = requestUser;
            putFlash("user", user);
        } else {
            user = new User();
            putFlash("user", null);
        }

        oldPass = "";

        //Carregando a lista de todos os usuários
        users = getDataTable();
    }

    public String form(User userEdit) {
        if (userEdit != null) {
            user = userBean.find(userEdit.getId());
            putFlash("user", user);
        } else {
            user = new User();
            putFlash("user", null);
        }

        if (roleList == null) {
            roleList = roleBean.findAll();
        }

        return "form";
    }

    public String save() {
        try {
            userBean.clearCache();
            if (user.getId() != null && user.getId() > 0) {
                userBean.edit(user);
                JsfUtil.addSuccessMessage("Usuário alterado com sucesso!");
            } else {
                User _user = userBean.findByProperty("email", user.getEmail());
                if (_user != null && !Objects.equals(_user.getId(), user.getId())) {
                    throw new Exception("E-mail já cadastrado!");
                }

                String string = user.getEmail() + new Date().toString();
                String password = "campomagro";
                String token = Criptografia.criptografar(string);
                //String password = Utils.randomPassword();
                user.setActivationToken(token);
                user.setPassword(Criptografia.criptografar(password));

                userBean.create(user);

//                sendMail(user, password, false);
                JsfUtil.addSuccessMessage("Usuário criado com sucesso!");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao salvar usuário: " + e.getMessage());
        }

        userBean.flush();
        setUsers(null);
        setUser(null);
        putFlash("user", null);

        return "list";
    }

    public void remove(User userForRemoval) {
        try {
            if (userForRemoval == null || userForRemoval.getId() == null || userForRemoval.getId() == 0) {
                throw new Exception("Usuário não definido.");
            }

            userBean.clearCache();
            userForRemoval = userBean.find(userForRemoval.getId());

            userBean.remove(userForRemoval);
            JsfUtil.addSuccessMessage("Usuário removido com sucesso!");

            userBean.flush();
            updateUserList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao remover usuário!");
        }

        try {
            reload();
        } catch (IOException e) {
            JsfUtil.addErrorMessage(e, "Falha ao recarregar formulário!");
        }
    }

    public Boolean renderPasswordFields() {
        return user.getId() != null && user.getId() > 0;
    }

    public String changePassword() {
        User loggedUser = userBean.find(getSessionUser().getId());
        try {

            String criptOldPass = Criptografia.criptografar(oldPass);

            if (!criptOldPass.equals(loggedUser.getPassword())) {
                JsfUtil.addErrorMessage("user.changePassword.oldPass.notMatch");
                return null;
            }

            loggedUser.setPassword(Criptografia.criptografar(user.getPassword()));

            userBean.edit(loggedUser);

            JsfUtil.addSuccessMessage("Sucesso ao editar/criar a senha.");

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao editar/criar a senha.");
            return null;
        }

        return "/index";
    }

    public String resetPassword() {
        try {
            User userReset = userBean.findByProperty("email", email);
            if (userReset == null) {
                throw new Exception("E-mail não cadastrado!");
            }
            String newPassword = PasswordGenerator.getRandomPassword();
            userReset.setPassword(Criptografia.criptografar(newPassword));
            userBean.edit(userReset);
            email = Base64.encodeAsString(userReset.getEmail());
            String url = Utils.SYSTEM_URL + "changePassword?e=" + email + "&t=" + userReset.getPassword();
            sendEmailResetPassword(userReset, url);
            JsfUtil.addSuccessMessage("Um link de redefinição de senha foi enviado para o seu e-mail.");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Não foi possível redefinir sua senha");
        }
        return "login";
    }

    public String newPassword() {
        try {
            user = userBean.findByProperty("email", changeEmail);
            user.setPassword(Criptografia.criptografar(newPass));
            userBean.edit(user);

            JsfUtil.addSuccessMessage("User.changePassword.success");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "User.changePassword.error");
            return null;
        }
        return "/login";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getSelectedRoleId() {
        return selectedRoleId;
    }

    public void setSelectedRoleId(Integer selectedRoleId) {
        this.selectedRoleId = selectedRoleId;
    }

    public List<Role> getRoleList() {
        if (roleList == null) {
            if (isUserInRole("ADMIN")) {
                roleList = roleBean.findAll();
            } else {
                roleList = roleBean.findAll(false);
            }
            Collections.sort(roleList, new RoleComparator());
        }
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChangeEmail() {
        if (changeEmail == null) {
            User u = (User) JsfUtil.getSessionAttribute(ChangePasswordFilter.CHANGE_PASSWORD_USER);
            if (u != null) {
                JsfUtil.setSessionAttribute(ChangePasswordFilter.CHANGE_PASSWORD_USER, null);
                changeEmail = u.getEmail();
            }
        }
        return changeEmail;
    }

    public void setChangeEmail(String changeEmail) {
        this.changeEmail = changeEmail;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public Role getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(Role systemRole) {
        this.systemRole = systemRole;
    }

    private void sendMail(User user, String password, boolean edit) {
        String message = "Sua conta foi cadastrada no sistema.";
        if (edit) {
            message = "Sua conta foi alterada no sistema.";
        }

        message += "\nSua senha é: " + password;
        message += "\n\nUtilize o link abaixo para confirmar seu cadastro.";
        message += "\n" + JsfUtil.getContextUrl() + "/activate?email=" + user.getEmail() + "&token=" + user.getActivationToken();
        Mail.getInstance().sendMail("CAMPO MAGRO - Cadastro Imobiliário", user.getEmail(), "Confirmação de Cadastro", message);
    }

    public void sendEmailResetPassword(final User user, final String url) {
        new Thread() {
            @Override
            public void run() {
                String subject = Utils.SYSTEM_NAME + " - Redefinição de senha";
                StringBuilder body = new StringBuilder();

                body.append(Utils.getGreeting()).append(", ").append(user.getName()).append("<br /><br />");
                body.append("Recebemos uma solicitação de redefinição da sua senha de acesso ao ").append(Utils.SYSTEM_NAME).append(". Acesse o link abaixo para definir uma nova senha:<br /><br />");
                body.append("<a href=\"").append(url).append("\" title=\"Redefinir senha de acesso ao ").append(Utils.SYSTEM_NAME).append("\">").append(url).append("</a>");
                body.append("<div align=\"center\"><small><i>Caso o link não funcione, copie-o e cole-o no navegador</i></small></div>");
                body.append("<br /><b>Atenciosamente,<br />");
                body.append("Equipe Campo Magro</b>");

                mailController.sendEmail(user.getEmail(), subject, body.toString());
            }
        }.start();
    }

    public void sendEmailToNewUser(final User user, final String userPass) {
        new Thread() {
            @Override
            public void run() {

                String subject = Utils.SYSTEM_NAME + " - Dados de acesso";
                StringBuilder body = new StringBuilder();

                body.append(Utils.getGreeting()).append(", ").append(user.getName()).append("<br /><br />");
                body.append("Seja bem vindo ao ").append(Utils.SYSTEM_NAME).append(". Seguem abaixo os seus dados de acesso<br /><br />");
                body.append("<b>E-mail de acesso:</b> ").append(user.getEmail()).append("<br />");
                body.append("<b>Senha:</b> ").append(userPass).append("<br /><br />");
                body.append("<b>Atenciosamente,<br />");
                body.append("Equipe Campo Magro</b>");

                mailController.sendEmail(user.getEmail(), subject, body.toString());
            }
        }.start();
    }

    public MailController getMailController() {
        return mailController;
    }

    public void setMailController(MailController mailController) {
        this.mailController = mailController;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public RoleBean getRoleBean() {
        return roleBean;
    }

    public void setRoleBean(RoleBean roleBean) {
        this.roleBean = roleBean;
    }

    public List<User> getDataTable() {
        if (users == null) {
            userBean.clearCache();
            users = userBean.findAll();
        }

        return this.users;
    }

    public void updateUserList() {
        users = null;
        users = getDataTable();
    }

    public List<User> lastUsers() {
        userBean.clearCache();
        return userBean.findRange(0, 10, "id", false, null);
    }

    public int compare(User obj1, User obj2) {
        Comparable comp1 = obj1.getName();
        Comparable comp2 = obj2.getName();
        return comp1.compareTo(comp2);
    }

}
