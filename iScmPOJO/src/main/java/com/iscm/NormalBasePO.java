package com.iscm;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/***
 * 非自动增长,基类PO
 */
@MappedSuperclass
public class NormalBasePO extends BasePO {

	private static final long serialVersionUID = 8741539399351260915L;
	protected long id;

	@Id
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
