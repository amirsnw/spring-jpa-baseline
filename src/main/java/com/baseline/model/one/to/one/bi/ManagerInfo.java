package com.baseline.model.one.to.one.bi;

import com.baseline.config.AppConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = AppConstants.TABLE_PREFIX + "manager_info")
public class ManagerInfo {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="linkedin_account")
	private String linkedinAccount;

	// Reference back to Manager
	@OneToOne(mappedBy="managerInfo",
			cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
						CascadeType.REFRESH}) // OneToOne default : FetchType.EAGER
	private Manager manager;
}







