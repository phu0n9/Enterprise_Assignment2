package controller;

import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import service.ReceivingService;

import java.util.Date;
import java.util.List;

@RequestMapping(path = "/")
@RestController
public class ReceivingNoteController {

    @Autowired
    private ReceivingService receivingService;

    @RequestMapping(path = "receiving",method = RequestMethod.POST)
    public List<ReceivingNote> addReceivingNote(@RequestBody List<ReceivingNote> receivingNote){
        return receivingService.saveReceivingNote(receivingNote);
    }

    @RequestMapping(path = "receiving",method = RequestMethod.PUT)
    public ReceivingNote updateReceivingNote(@RequestBody ReceivingNote receivingNote){
        return receivingService.updateReceivingNote(receivingNote);
    }

    @RequestMapping(path = "receiving/{id}",method = RequestMethod.GET)
    public ReceivingNote getReceivingNote(@PathVariable int id){
        return receivingService.getReceivingNote(id);
    }

    @RequestMapping(path = "receiving/{id}",method = RequestMethod.DELETE)
    public int deleteReceivingNote(@PathVariable int id){
        return receivingService.deleteReceivingNote(id);
    }

    @RequestMapping(path = "receiving/details",method = RequestMethod.POST)
    public List<ReceivingNoteDetails> addReceivingNoteDetails (@RequestBody List<ReceivingNoteDetails> receivingNoteDetails){
        return receivingService.saveReceivingNoteDetails(receivingNoteDetails);
    }

    @RequestMapping(path = "receiving/date", method = RequestMethod.GET)
    public List<ReceivingNote> pagingSearchReceivingNoteByDate(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date date, @RequestParam Integer limit, @RequestParam Integer start) {
        return receivingService.multipleSearchByDate(date, limit, start);
    }

    @RequestMapping(path = "receiving/period", method = RequestMethod.GET)
    public List<ReceivingNote> searchByPeriodOfDate(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date endDate, @RequestParam Integer limit, @RequestParam Integer start){
        return receivingService.searchByPeriodOfDate(startDate,endDate,limit,start);
    }


}
