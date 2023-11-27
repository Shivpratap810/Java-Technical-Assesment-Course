package com.test.shiv.jta.service;

import com.test.shiv.jta.entity.Course;
import com.test.shiv.jta.entity.Enrollment;
import com.test.shiv.jta.exception.BadRequestException;
import com.test.shiv.jta.exception.ResourceNotFoundException;
import com.test.shiv.jta.repository.CourseRepository;
import com.test.shiv.jta.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found with id: " + courseId));
    }

    public Course addCourse(Course course) {
        if (course == null || course.getCourseName().isEmpty() || course.getCourseInstructor().isEmpty()) {
            throw new BadRequestException("Invalid request");
        }
        return courseRepository.save(course);
    }

    public Course updateCourse(Long courseId, Course updatedCourse) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found with id: " + courseId));
        if (updatedCourse == null || updatedCourse.getCourseName().isEmpty() || updatedCourse.getCourseInstructor().isEmpty()) {
            throw new BadRequestException("Invalid request");
        } else {
            existingCourse.setCourseName(updatedCourse.getCourseName());
            existingCourse.setCourseDescription(updatedCourse.getCourseDescription());
            existingCourse.setCourseInstructor(updatedCourse.getCourseInstructor());
            return courseRepository.save(existingCourse);
        }
    }

    public void deleteCourse(Long courseId) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found with id: " + courseId));
        List<Enrollment> existingEnrollments = enrollmentRepository.findByCourse(existingCourse);
        enrollmentRepository.deleteAll(existingEnrollments);
        courseRepository.deleteById(courseId);
    }
}
