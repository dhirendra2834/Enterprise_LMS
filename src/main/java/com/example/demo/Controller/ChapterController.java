package com.example.demo.Controller;

import com.example.demo.repository.ChapterRepository;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.DTO.ChapterRequestDto;
import com.example.demo.DTO.ChapterResponseDto;
import com.example.demo.Service.ChapterService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@AllArgsConstructor
@Controller
@RequestMapping("/chapters")

public class ChapterController {

	private final ChapterRepository chapterRepository;
	private ChapterService chapterService;

//	create Chapter inside the Particular Subject
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@PostMapping
	public ResponseEntity<String> createChapter(@RequestBody ChapterRequestDto chapter) {
		String createChapterOrNot= chapterService.createChapter(chapter);
		return ResponseEntity.ok(createChapterOrNot);
	}
	
//	Get Chapter details by Chapter Id;
	@GetMapping("/{Id}")
	public ResponseEntity<ChapterResponseDto> getChapterById(@PathVariable int Id){
		ChapterResponseDto  chapterResponseDto= chapterService.getChapterDetailsById(Id);
		return ResponseEntity.ok(chapterResponseDto);
	}

//	Get All Chapter By Subject Id
	@GetMapping("/subject/{Id}")
	public ResponseEntity<List<ChapterResponseDto>> getChaptersBySubjectId(@PathVariable Integer Id){
		List<ChapterResponseDto> chapterResponseDto = chapterService.getChaptersBySubjectId(Id);
		return ResponseEntity.ok(chapterResponseDto);
	}
	
//	Update the Chapter details By Id
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@PutMapping("/{Id}")
	public ResponseEntity<String> updateChapterDetailsById(@RequestBody ChapterRequestDto chapterRequestDto ,@PathVariable int Id){
		String chapterUpdated = chapterService.updateChapterDetailsById(chapterRequestDto,Id);
		return ResponseEntity.ok(chapterUpdated);
	}
	
//	delete the Chapter By Id
	@PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATION')")
	@DeleteMapping("{Id}")
	public ResponseEntity<String> deleteChapterById(@PathVariable int Id){
		String chapterDeleted= chapterService.deleteChapterDetails(Id);
		return ResponseEntity.ok(chapterDeleted);
		
	}
	
}
