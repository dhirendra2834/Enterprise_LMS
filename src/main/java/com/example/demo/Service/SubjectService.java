package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.SubjectResponseDto;
import com.example.demo.model.Subject;
import com.example.demo.repository.SubjectRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SubjectService {

	
	private SubjectRepository subjectRepository;
	
//	update the SubjectData by subjectId
	public String updateSubjectData_subjectId(Subject subject, int id) {
		
	Optional<Subject> subjectExits= subjectRepository.findById(id);
		
		if(subjectExits.isEmpty()) {
	        throw new RuntimeException("This subject is not exits");
		};
         
		Subject existingSubjectData= subjectExits.get();
		existingSubjectData.setName(subject.getName());
		existingSubjectData.setDescription(subject.getDescription());
		existingSubjectData.setChapters(subject.getChapters());
		existingSubjectData.setCourses(subject.getCourses());
		
		subjectRepository.save(existingSubjectData);
		
		return "latest subject data is updated";
	}
	
//	get subjectData By subjectId
	
	public SubjectResponseDto getSubjectById(int id) {
		Optional<Subject> subjectExits= subjectRepository.findById(id);
		
		if(subjectExits.isEmpty()) {
	        throw new RuntimeException("This subject is not exits");
		};
		
		SubjectResponseDto subjectResponseDto= 
				SubjectResponseDto.builder()
				.name(subjectExits.get().getName())
				.description(subjectExits.get().getDescription())
				.chapters(subjectExits.get().getChapters())
				.id(subjectExits.get().getId())
				.courses(subjectExits.get().getCourses())
				.build();
		
		return subjectResponseDto;
	}
//	get all Subject data By CourseId
	public List<SubjectResponseDto> fetchAllSubjectByCourseId(int id) {
		
//		Course courseExits 
	
		return null;
	}
	
//	delete the subject by SubjectId
	public String deleteSubjectBysubjectId(int id) {
		Optional<Subject> subjectExits= subjectRepository.findById(id);
		
		if(subjectExits.isEmpty()) {
			return "This Subject is not exits"; 
		}
		
		subjectRepository.deleteById(id);
		return "subject "+subjectExits.get().getName()+" is deleted sucessfully";
	}

	
}
