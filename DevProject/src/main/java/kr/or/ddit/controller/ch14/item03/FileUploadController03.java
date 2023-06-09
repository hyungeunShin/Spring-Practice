package kr.or.ddit.controller.ch14.item03;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.controller.ch14.item03.service.ItemService3;
import kr.or.ddit.vo.Item3;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/item3")
@Slf4j
public class FileUploadController03 {
	/*
	 * 비동기 방식 업로드
	 * - 비동기 방식으로 여러 개의 이미지를 업로드 하는 파일 업로드 기능을 구현
	 * 
	 * 	1. 환경설정
	 * 		1) 의존 관계 정의
	 * 		- commons-io: 파일을 처리하기 위한 의존 라이브러리
	 * 		- imgscalr-lib: 이미지 변환을 처리하기 위한 의존 라이브러리
	 * 		- jackson-databind: json데이터 바인딩을 위한 의존 라이브러리
	 * 
	 */
	//root-context.xml에서 설정한 uplaodPath
	@Resource(name="uploadPath")
	private String resourcePath;
	
	@Inject
	private ItemService3 itemService;
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String itemRegisterForm() {
		return "ch14_3/register";
	}
	
	//uploadAjax 메소드는 브라우저에서 넘겨받은 파일을 업로드 하는 기능
	@ResponseBody
	@RequestMapping(value="/uploadAjax", method=RequestMethod.POST, produces="text/plain; charset=utf-8")
	public ResponseEntity<String> uploadAjax(MultipartFile file) throws Exception {
		log.info("name : " + file.getOriginalFilename());
		
		String savedName = UploadFileUtils.uploadFile(resourcePath, file.getOriginalFilename(), file.getBytes());
		return new ResponseEntity<String>(savedName, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String item3Register(Item3 item, Model model) {
		String[] files = item.getFiles();
		
		if(files != null) {
			for (int i = 0; i < files.length; i++) {
				log.info("files[" + i + "] : " + files[i]);
			}
			
			itemService.register(item);
			model.addAttribute("msg", "등록이 완료되었습니다");
			return "ch14_3/success";
		} else {
			model.addAttribute("msg", "등록을 실패하였습니다");
			return "ch14_3/success";
		}
		
	}
	
	@RequestMapping(value="/displayFile")
	public ResponseEntity<byte[]> displayFile(String fileName) {
		ResponseEntity<byte[]> entity = null;
		
		try(InputStream in = new FileInputStream(resourcePath + fileName)) {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
			MediaType mtype = MediaUtils.getMediaType(formatName);
			HttpHeaders headers = new HttpHeaders();
		
			if(mtype != null) {
				headers.setContentType(mtype);
			} else {
				fileName = fileName.substring(fileName.indexOf("_") + 1);
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition", "attachment; fileName\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
			}
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String item3List(Model model) {
		List<Item3> list = itemService.list();
		model.addAttribute("itemList", list);
		return "ch14_3/list";
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.GET)
	public String modifyForm(int itemId, Model model) {
		Item3 item = itemService.read(itemId);
		model.addAttribute("item", item);
		return "ch14_3/modify";
	}
	
	@ResponseBody
	@RequestMapping(value="/getAttach/{itemId}")
	public List<String> getAttach(@PathVariable int itemId) {
		log.info("id : " + itemId);
		return itemService.getAttach(itemId);
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String item3Modify(Item3 item, Model model) {
		itemService.modify(item);
		model.addAttribute("msg", "수정이 완료되었습니다");
		return "ch14_3/success";
	}
	
	@RequestMapping(value="/remove", method=RequestMethod.GET)
	public String itemRemoveForm(int itemId, Model model) {
		Item3 item = itemService.read(itemId);
		model.addAttribute("item", item);
		return "ch14_3/remove";
	}
	
	@RequestMapping(value="/remove", method=RequestMethod.POST)
	public String itemRemove(int itemId, Model model) {
		itemService.remove(itemId);
		model.addAttribute("msg", "삭제되었습니다");
		return "ch14_3/success";
	}
}
