package com.hiredesk.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    // EMAIL IS MOCKED FOR NOW
    // We will plug in real Gmail SMTP at the end

    public void sendFeedbackRequest(
            String toEmail,
            String interviewerName,
            String candidateName,
            String feedbackLink) {

        // TODO: Replace with real email later
        log.info("📧 FEEDBACK REQUEST EMAIL");
        log.info("To: {}", toEmail);
        log.info("Interviewer: {}", interviewerName);
        log.info("Candidate: {}", candidateName);
        log.info("Link: {}", feedbackLink);
        log.info("─────────────────────────────");
    }

    public void sendFeedbackReminder(
            String toEmail,
            String interviewerName,
            String candidateName,
            String feedbackLink) {

        log.info("📧 FEEDBACK REMINDER EMAIL");
        log.info("To: {}", toEmail);
        log.info("Interviewer: {}", interviewerName);
        log.info("Candidate: {}", candidateName);
        log.info("Link: {}", feedbackLink);
        log.info("─────────────────────────────");
    }

    public void sendInterviewConfirmation(
            String toEmail,
            String candidateName,
            String interviewDate,
            String meetingLink) {

        log.info("📧 INTERVIEW CONFIRMATION EMAIL");
        log.info("To: {}", toEmail);
        log.info("Candidate: {}", candidateName);
        log.info("Date: {}", interviewDate);
        log.info("Meet Link: {}", meetingLink);
        log.info("─────────────────────────────");
    }

    public void sendStatusUpdate(
            String toEmail,
            String candidateName,
            String status,
            String jobTitle) {

        log.info("📧 STATUS UPDATE EMAIL");
        log.info("To: {}", toEmail);
        log.info("Candidate: {}", candidateName);
        log.info("Status: {}", status);
        log.info("Job: {}", jobTitle);
        log.info("─────────────────────────────");
    }

    public void sendOfferLetter(
            String toEmail,
            String candidateName,
            String jobTitle,
            String salary) {

        log.info("📧 OFFER LETTER EMAIL");
        log.info("To: {}", toEmail);
        log.info("Candidate: {}", candidateName);
        log.info("Job: {}", jobTitle);
        log.info("Salary: {}", salary);
        log.info("─────────────────────────────");
    }
}