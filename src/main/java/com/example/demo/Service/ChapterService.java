package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.ChapterRequestDto;
import com.example.demo.DTO.ChapterResponseDto;
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
	
//	get Chapter details By Chapter Id
	public ChapterResponseDto getChapterDetailsById(int Id) {
		
		Optional<Chapter> chapterExits= chapterRepository.findById(Id);
		
		 if (chapterExits.isEmpty()) {
		        throw new RuntimeException("This chapter is not exits");		
		   }
		 
		 ChapterResponseDto chapterResponseDto=
				 ChapterResponseDto.builder()
				 .name(chapterExits.get().getName())
		         .id(chapterExits.get().getId())
		         .description(chapterExits.get().getDescription())
		         .chapterOrder(chapterExits.get().getChapterOrder())
		         .subject(chapterExits.get().getSubject())
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
	                    .subject(chapter.getSubject())
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
