import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselPrevious,
  CarouselNext,
} from "@/components/ui/carousel";
import StoryCardSkeleton from "./story-card-skeleton";
import { cn } from "@/lib/utils";

type Props = {
  className?: string;
};

const StoryPickerSkeleton = ({ className }: Props) => (
  <Carousel className={cn("w-full md:w-[90%] self-center", className)}>
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
