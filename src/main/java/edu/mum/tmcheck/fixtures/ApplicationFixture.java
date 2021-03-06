package edu.mum.tmcheck.fixtures;

import edu.mum.tmcheck.serviceimp.IdCardServiceImp;
import edu.mum.tmcheck.services.IdCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ApplicationFixture {
    final int DEFAULT_NUMBER_OF_RECORDS = 10;

    @Autowired
    CardFixture cardFixture;

    @Autowired
    EntryFixture entryFixture;

    @Autowired
    CourseFixture courseFixture;

    @Autowired
    BlockFixture blockFixture;

    @Autowired
    FacultyFixture facultyFixture;

    @Autowired
    OfferedCourseFixture offeredCourseFixture;

    @Autowired
    LocationFixture locationFixture;

    @Autowired
    MeditationTypeFixture meditationTypeFixture;

    @Autowired
    StudentFixture studentFixture;

    @Autowired
    AdminFixture adminFixture;

    @Autowired
    AttendanceFixture attendanceFixture;

    @Autowired
    IdCardServiceImp idCardServiceImp;

    @PostConstruct
    public void initialize() {
        entryFixture.generate(2);
        courseFixture.generate(DEFAULT_NUMBER_OF_RECORDS);
        cardFixture.generate(120);
        blockFixture.generate(DEFAULT_NUMBER_OF_RECORDS);
        facultyFixture.generate(DEFAULT_NUMBER_OF_RECORDS);
        offeredCourseFixture.generate(60);
        locationFixture.generate(DEFAULT_NUMBER_OF_RECORDS);
        meditationTypeFixture.generate(DEFAULT_NUMBER_OF_RECORDS);
        studentFixture.generate(idCardServiceImp.findAll().size());
        adminFixture.generate(DEFAULT_NUMBER_OF_RECORDS);
        attendanceFixture.generate(200);
    }
}
