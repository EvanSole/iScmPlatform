package com.iscm;

import javax.persistence.*;

/***
 * 自动增长,基类PO
 */
@MappedSuperclass
public class AutoBasePO extends BasePO {

	private static final long serialVersionUID = -4527499105191666161L;
	
	protected long id;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
