package com.test.shiv.jta.service;

import com.test.shiv.jta.entity.Course;
import com.test.shiv.jta.entity.Enrollment;
import com.test.shiv.jta.entity.Student;
import com.test.shiv.jta.repository.CourseRepository;
import com.test.shiv.jta.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Optional<Course> existingCourse = courseRepository.findById(id);
        if (existingCourse.isPresent()) {
            Course course = existingCourse.get();
            course.setCourseName(updatedCourse.getCourseName());
            course.setCourseDescription(updatedCourse.getCourseDescription());
            course.setCourseInstructor(updatedCourse.getCourseInstructor());
            return courseRepository.save(course);
        } else {
            // Handle course not found
            return null;
        }
    }

    public void deleteCourse(Long courseId) {
        Optional<Course> existingCourse = courseRepository.findById(courseId);
        if(existingCourse.isPresent()) {

            List<Enrollment> existingEnrollments = enrollmentRepository.findByCourse(existingCourse.get());
            enrollmentRepository.deleteAll(existingEnrollments);
            courseRepository.deleteById(courseId);
        } else {
            System.out.println("Course Not Present");
        }
    }
}
