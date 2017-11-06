package org.domain.sck.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


@Entity
@Table(name = "attachment")
public class Attachment {


	private Long id;
	private String name;
	private long size;
	private String contentType;
	private byte[] data;
	
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() { return this.id; }
	public void setId(Long id) { this.id = id; }
	
	@Column(name = "name")
	public String getName() { return this.name;	}
	public void setName(String name) { this.name = name; }
	
	@Column(name = "size")
	public long getSize() { return this.size; }
	public void setSize(long size) { this.size = size; }
	
	@Column(name = "contenttype")
	public String getContentType() { return this.contentType; }
	public void setContentType(String contentType) { this.contentType = contentType; }
	
	@Lob
	@Column(name = "data" ,length = 2147483647)
	@Basic(fetch = FetchType.LAZY)
	public byte[] getData() { return this.data; }
	public void setData(byte[] data) { this.data = data; }
	
	
}