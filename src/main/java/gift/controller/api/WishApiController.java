package gift.controller.api;

import gift.annotation.LoginMember;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Wish;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishApiController {

    private final WishService wishService;
    public WishApiController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<Void> addWish(@Valid @RequestBody WishRequestDto request, @LoginMember Member loginMember) {
        Wish savedWish = wishService.addWish(loginMember, request);
        URI location = URI.create("/api/wishes/" + savedWish.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishes(@LoginMember Member loginMember) {
        List<WishResponseDto> wishes = wishService.getWishesByMember(loginMember);
        return ResponseEntity.ok(wishes);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish( @PathVariable Long wishId, @LoginMember Member loginMember) {
        wishService.deleteWish(wishId, loginMember);
        return ResponseEntity.noContent().build();
    }
}