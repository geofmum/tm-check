package edu.mum.tmcheck.controllers;

import edu.mum.tmcheck.domain.entities.Block;
import edu.mum.tmcheck.domain.entities.Entry;
import edu.mum.tmcheck.domain.entities.User;
import edu.mum.tmcheck.serviceimp.*;
import edu.mum.tmcheck.services.ExcelReportGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.format.TextStyle;
import java.util.*;

@Controller
@RequestMapping(value = "/reports")
public class ReportController {
    @Autowired
    EntryServiceImp entryServiceImp;

    @Autowired
    BlockServiceImp blockServiceImp;

    @Autowired
    EntryAttendanceReportServiceImp entryAttendanceReportServiceImp;

    @Autowired
    AttendanceServiceImp attendanceServiceImp;

    @Autowired
    UserServiceImp userServiceImp;

    @Autowired
    BlockAttendanceReportServiceImp blockAttendanceReportServiceImp;

    @Autowired
    ExcelReportGeneratorService excelReportGeneratorService;

    @Autowired
    AuthenticationServiceImp authenticationServiceImp;

    @Autowired
    ECAttendanceReportServiceImp ecAttendanceReportServiceImp;

    @Autowired
    OfferedCourseServiceImp offeredCourseServiceImp;

    @Autowired
    FacultyServiceImp facultyServiceImp;

    @ModelAttribute
    public void prepareReportsLists(Model model) {
        Map<String, String> reports = new HashMap<String, String>() {{
            put("Entry Attendance Report", "/reports/entry-attendance-report");
            put("Extra Credit Attendance Report", "/reports/ec-attendance-report");
        }};

        model.addAttribute("availableReports", reports);
    }

    @GetMapping(value = {"/entry-attendance-report/{entryId}", "/entry-attendance-report"})
    public String entryAttendanceReport(@PathVariable(name = "entryId", required = false) Optional<Long> entryId, Model model) {
        String reportTitle = "Entry Attendance Report";
        String reportKey = "entry-attendance-report";

        Entry currentEntry = entryServiceImp.findById(entryId.orElse(1L));
        model.addAttribute("currentEntry", currentEntry);
        model.addAttribute("downloadLink", entryAttendanceReportServiceImp.downloadLink(currentEntry.getId()));

        Map<String, String> reports = (HashMap<String, String>) model.asMap().get("availableReports");
        reports.remove(reportTitle);

        model.addAttribute("pageTitle", reportTitle);
        model.addAttribute("reportTitle", reportTitle);
        model.addAttribute("reportKey", reportKey);

        List<Entry> entries = entryServiceImp.findAll();
        model.addAttribute("entries", entries);

        model.addAttribute("reportData", entryAttendanceReportServiceImp.generateByEntry(currentEntry.getName()));

        model.addAttribute("downloadlink", "/download/entry-attendance-report/" + currentEntry.getId() + ".xlsx");

        return "entry-attendance-report";
    }

    @GetMapping(value = {"/ec-attendance-report/{blockId}", "/ec-attendance-report"})
    public String ecAttendanceReport(@PathVariable(name = "blockId", required = false) Optional<Long> blockId, Model model, Principal principal) throws Exception {
        if (principal.getName() == null)
            return "redirect:/login";

        User user = authenticationServiceImp.getAuthenticatedUserByUsername(principal.getName());

        String reportTitle = "Extra Credit Attendance Report";
        String reportKey = "ec-attendance-report";

        Map<String, String> reports = (HashMap<String, String>) model.asMap().get("availableReports");
        reports.remove(reportTitle);


        model.addAttribute("pageTitle", reportTitle);
        model.addAttribute("reportTitle", reportTitle);
        model.addAttribute("reportKey", reportKey);

        List<Block> blocks = blockServiceImp.findAllByUserId(user.getId());
        HashMap<Long, String> blks = new HashMap<>();
        blocks.forEach(b -> {
            blks.put(b.getId(), offeredCourseServiceImp.getbyblockandfaculty(b, facultyServiceImp.findById(user.getId())).getCourse().getName() + "  " +b.getStartDate().plusDays(5).getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "  " + b.getStartDate().plusDays(8).getYear());
        });

        model.addAttribute("blocks", blks);

        long defaultBlockId = blocks.get(0) != null ? blocks.get(0).getId() : 0;
        Block currentBlock = blockServiceImp.findById(blockId.orElse(defaultBlockId));
        model.addAttribute("currentBlock", currentBlock);
        model.addAttribute("currentBlockName", offeredCourseServiceImp.getbyblockandfaculty(currentBlock, facultyServiceImp.findById(user.getId())).getCourse().getName() + "  "+currentBlock.getStartDate().plusDays(5).getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) +"  "+ currentBlock.getStartDate().plusDays(8).getYear());
        model.addAttribute("blockid", currentBlock.getId());

        model.addAttribute("reportData", ecAttendanceReportServiceImp.findAllByFacultyIdAndBlockId(user.getId(), currentBlock.getId()));
        model.addAttribute("downloadlink", "/download/ec-attendance-report/" + blockId.orElse(defaultBlockId) + ".xlsx");

        return "ec-attendance-report";
    }
}
