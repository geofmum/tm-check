package edu.mum.tmcheck.services;

import edu.mum.tmcheck.domain.entities.Block;
import edu.mum.tmcheck.domain.entities.Faculty;
import edu.mum.tmcheck.domain.entities.OfferedCourse;

import java.util.List;

public interface OfferedCourseService {
    public void create();

    //    public void update();
    public OfferedCourse get();

    public OfferedCourse save(OfferedCourse instance);

    public List<OfferedCourse> findAll();

    public List<Block>  getfacultytaughtblock(Long userid);

    public OfferedCourse getbyblockandfaculty(Block block, Faculty faculty);

    public List<OfferedCourse> findAllByFacultyId(long facultyId);

}
