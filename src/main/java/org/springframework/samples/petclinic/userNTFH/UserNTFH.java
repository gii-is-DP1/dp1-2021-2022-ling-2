package org.springframework.samples.petclinic.userNTFH;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usersNTFH")

public class UserNTFH {
    @Id
    String username;
    boolean isRegistered;
//  In future we will change the String stats to another entity called StatsType, for now, to ensure the java file compile, we put an String, in the end, it will be: StatsType stats
    String stats;
//  In future we will change the String achievements to another entity called List<Achievements>, for now, to ensure the java file compile, we put an String, in the end, it will be: List<String> achievements
    List<String> achievements;
    Boolean isBanned;
}
