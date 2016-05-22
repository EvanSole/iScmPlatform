package com.iscm.po.main;

import com.iscm.AutoBasePO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_iscm_user")
public class IScmUserEntity extends AutoBasePO {

	private static final long serialVersionUID = -321388108179316139L;

    private Long tenantId;
	private String userName;
	private String password;
	private String realName;
	private String email;
	private String telephone;
	private String mobile;
	private Byte isAdmin;
	private Byte isActive;
	private Long expiredDate;
	private Byte isDel;
    private Byte isLocked;

    @Basic
    @Column(name = "tenant_id", nullable = false, insertable = true, updatable = true)
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Basic
	@Column(name = "user_name", nullable = false, insertable = true, updatable = true, length = 25)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Basic
	@Column(name = "password", nullable = false, insertable = true, updatable = true, length = 100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    @Basic
    @Column(name = "real_name", nullable = true, insertable = true, updatable = true, length = 25)
    public String getRealName() {
        return realName;
    }

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Basic
	@Column(name = "email", nullable = true, insertable = true, updatable = true, length = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Basic
	@Column(name = "telephone", nullable = true, insertable = true, updatable = true, length = 25)
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Basic
	@Column(name = "mobile", nullable = true, insertable = true, updatable = true, length = 25)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Basic
	@Column(name = "is_admin", nullable = false, insertable = true, updatable = true)
	public Byte getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Byte isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Basic
	@Column(name = "is_active", nullable = false, insertable = true, updatable = true)
	public Byte getIsActive() {
		return isActive;
	}

	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	@Basic
	@Column(name = "expired_date", nullable = false, insertable = true, updatable = true)
	public Long getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Long expiredDate) {
		this.expiredDate = expiredDate;
	}

	@Basic
	@Column(name = "is_del", nullable = false, insertable = true, updatable = true)
	public Byte getIsDel() {
		return isDel;
	}

	public void setIsDel(Byte isDel) {
		this.isDel = isDel;
	}

    @Basic
    @Column(name = "is_locked", nullable = false, insertable = true, updatable = true)
    public Byte getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Byte isLocked) {
        this.isLocked = isLocked;
    }
}
