import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselPrevious,
  CarouselNext,
} from "@/components/ui/carousel";
import StoryCardSkeleton from "./story-card-skeleton";

const StoryPickerSkeleton = () => (
  <Carousel className="w-full md:w-[90%] self-center">
    <CarouselContent>
      <CarouselItem className="basis-full md:basis-1/2 xl:basis-1/3">
        <StoryCardSkeleton />
      </CarouselItem>
      <CarouselItem className="basis-full md:basis-1/2 xl:basis-1/3">
        <StoryCardSkeleton />
      </CarouselItem>
      <CarouselItem className="basis-full md:basis-1/2 xl:basis-1/3">
        <StoryCardSkeleton />
      </CarouselItem>
    </CarouselContent>
    <CarouselPrevious className="hidden md:flex" />
    <CarouselNext className="hidden md:flex" />
  </Carousel>
);

export default StoryPickerSkeleton;
