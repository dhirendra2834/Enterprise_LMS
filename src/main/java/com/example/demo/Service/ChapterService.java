package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.ChapterRequestDto;
import com.example.demo.DTO.ChapterResponseDto;
import com.example.demo.DTO.Chapter_SubjectDto;
import com.example.demo.DTO.Subject_CourseDto;
import com.example.demo.model.Chapter;
import com.example.demo.model.Subject;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.SubjectRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ChapterService {
	
	private ChapterRepository chapterRepository;
	
	private SubjectRepository subjectRepository;

//	create the Chapter
	public String createChapter(ChapterRequestDto chapter) {
		Chapter chapterExits= chapterRepository.findByName(chapter.getName(), chapter.getSubjectId());
		
		 if (chapterExits != null) {
		        return "This chapter already exists in this subject";
		    }
		 
		 Subject subject = subjectRepository
		            .findById(chapter.getSubjectId())
		            .orElseThrow(() ->
		                    new RuntimeException("Subject does not exist"));

		    Chapter chapterData = Chapter.builder()
		            .name(chapter.getName())
		            .description(chapter.getDescription())
		            .chapterOrder(chapter.getChapterOrder())
		            .subject(subject)
		            .build();

		    chapterRepository.save(chapterData);

		    return "Chapter " + chapterData.getName()
		            + " created successfully";
	}

    // Convert Course Entity -> Subject_CourseDto
	public List<Subject_CourseDto> covertCourseToSubject_CourseDto(Subject subject){
		List<Subject_CourseDto> courses =
				subject.getCourses()
					.stream()
					.map(course -> Subject_CourseDto.builder()
                            .id(course.getId())
                            .name(course.getName())
                            .description(course.getDescription())
                            .build())
                    .toList();
    return courses;
	}

    // Convert Subject Entity -> Chapter_SubjectDto
	public Chapter_SubjectDto convertSubjecttoChapter_SubjectDto(Subject subject) {
		
		 List<Subject_CourseDto> courses= covertCourseToSubject_CourseDto(subject);

		 Chapter_SubjectDto subjectData= Chapter_SubjectDto.builder()
			      .id(subject.getId())
			      .name(subject.getName())
			      .description(subject.getDescription())
			      .courses(courses)
				 .build();
		 return subjectData;
	}

//	get Chapter details By Chapter Id
	public ChapterResponseDto getChapterDetailsById(int Id) {
		
		Optional<Chapter> chapterExits= chapterRepository.findById(Id);
		
		 if (chapterExits.isEmpty()) {
		        throw new RuntimeException("This chapter is not exits");		
		   }
	       
		 Chapter_SubjectDto subject=convertSubjecttoChapter_SubjectDto(chapterExits.get().getSubject());

				 
		 ChapterResponseDto chapterResponseDto=
				 ChapterResponseDto.builder()
				 .name(chapterExits.get().getName())
		         .id(chapterExits.get().getId())
		         .description(chapterExits.get().getDescription())
		         .chapterOrder(chapterExits.get().getChapterOrder())
		         .subject(subject)
				 .build();
		 
		 return chapterResponseDto;
		 
	}

//	get All Chapter By SubjectId
	public List<ChapterResponseDto> getChaptersBySubjectId(Integer id) {

	    // Check whether Subject exists
	    Subject subject = subjectRepository.findById(id)
	            .orElseThrow(() ->
	                    new RuntimeException("Subject does not exist"));

	    // Get all chapters of this subject
	    List<Chapter> chapters =
	            chapterRepository.findAllBySubject_Id(id);

	    // Convert Entity to Response DTO
	    return chapters.stream()
	            .map(chapter -> ChapterResponseDto.builder()
	                    .id(chapter.getId())
	                    .name(chapter.getName())
	                    .description(chapter.getDescription())
	                    .chapterOrder(chapter.getChapterOrder())
	                    .subject(convertSubjecttoChapter_SubjectDto(chapter.getSubject()))
	                    .build())
	            .toList();
	}
//	update the Chapter details By Id
	public String updateChapterDetailsById(
	        ChapterRequestDto chapter,
	        int id) {

	    // Find existing chapter
	    Chapter existingChapter = chapterRepository.findById(id)
	            .orElseThrow(() ->
	                    new RuntimeException("Chapter does not exist"));

	    // Check whether the new subject exists
	    Subject subject = subjectRepository
	            .findById(chapter.getSubjectId())
	            .orElseThrow(() ->
	                    new RuntimeException("Subject does not exist"));

	    // Check duplicate chapter name inside the same subject
	    Chapter chapterExists =
	            chapterRepository.findByNameAndSubject_Id(
	                    chapter.getName(),
	                    chapter.getSubjectId());

	    if (chapterExists != null
	            && !chapterExists.getId().equals(id)) {

	        return "Chapter with this name already exists in this subject";
	    }

	    // Update existing chapter
	    existingChapter.setName(chapter.getName());
	    existingChapter.setDescription(chapter.getDescription());
	    existingChapter.setChapterOrder(chapter.getChapterOrder());
	    existingChapter.setSubject(subject);

	    // Save existing entity
	    chapterRepository.save(existingChapter);

	    return "Chapter " + existingChapter.getName()
	            + " updated successfully";
	}
	
//	 delete the Chapter data by Id 
	public String deleteChapterDetails(int id) {

	    Chapter chapter = chapterRepository.findById(id)
	            .orElseThrow(() ->
	                    new RuntimeException("Chapter does not exist"));

	    String chapterName = chapter.getName();

	    chapterRepository.delete(chapter);

	    return "Chapter " + chapterName + " deleted successfully";
	}
}
