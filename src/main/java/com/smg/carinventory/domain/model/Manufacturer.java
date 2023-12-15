package com.smg.carinventory.domain.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="manufacturer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manufacturer_id_generator")
	@SequenceGenerator(name="manufacturer_id_generator", sequenceName = "manufacturer_id_seq", allocationSize = 1)
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "country")
	private String country;
	@Column(name = "from_year")
	private Integer fromYear;
	@Column(name = "to_year")
	private Integer toYear;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Manufacturer other = (Manufacturer) obj;
		return Objects.equals(name, other.name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
		
}
