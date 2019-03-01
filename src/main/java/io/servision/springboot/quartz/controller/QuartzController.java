package io.servision.springboot.quartz.controller;

import io.servision.springboot.quartz.entity.QuartzDto;
import io.servision.springboot.quartz.entity.QuartzVo;
import io.servision.springboot.quartz.service.SchedulerService;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author sun 2019/3/1
 */
@RestController
@RequestMapping("/quartz")
public class QuartzController {

	@Autowired
	private SchedulerService schedulerService;

	@GetMapping
	public List<QuartzVo> queryAll() throws SchedulerException {
		return schedulerService.queryAll();
	}

	@PostMapping
	public ResponseEntity add(@RequestBody QuartzDto quartzDto) {

		schedulerService.add(quartzDto.getJobName(), quartzDto.getJobGroup(), quartzDto.getCronExpression(),
				quartzDto.getJobDescription());
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity update(@RequestBody QuartzDto quartzDto) {
		schedulerService.update(quartzDto.getJobName(), quartzDto.getJobGroup(), quartzDto.getCronExpression(),
				quartzDto.getJobDescription());
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity delete(@RequestBody QuartzDto quartzDto) {
		schedulerService.delete(quartzDto.getJobName(), quartzDto.getJobGroup());
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping("/pause")
	public ResponseEntity pause(@RequestBody QuartzDto quartzDto) {
		schedulerService.pause(quartzDto.getJobName(), quartzDto.getJobGroup());
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping("/resume")
	public ResponseEntity resume(@RequestBody QuartzDto quartzDto) {
		schedulerService.resume(quartzDto.getJobName(), quartzDto.getJobGroup());
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping("/trigger")
	public ResponseEntity trigger(@RequestBody QuartzDto quartzDto) {
		schedulerService.trigger(quartzDto.getJobName(), quartzDto.getJobGroup());
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping("/query")
	public JobDetail query(@RequestBody QuartzDto quartzDto)
			throws SchedulerException {
		return schedulerService.query(quartzDto.getJobName(), quartzDto.getJobGroup());
	}


}
