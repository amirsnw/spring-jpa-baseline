package com.baseline.entity.one.to.one.sharedPK.uniPK;

import com.baseline.config.AppConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = AppConstants.TABLE_PREFIX + "manuscript")
public class ManuscriptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manuscript_sequence")
    @SequenceGenerator(name = "manuscript_sequence", sequenceName = "manu_seq", allocationSize = 1)
    @Column(name = "man_id")
    private int id;

    @Column(name = "water_mark")
    private String waterMark;

    @OneToOne
    @JoinColumn(name = "man_id")
    @MapsId
    private BookEntity book;
}
