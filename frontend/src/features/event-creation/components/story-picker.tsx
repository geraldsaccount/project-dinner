import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselPrevious,
  CarouselNext,
} from "@/components/ui/carousel";
import StoryCard from "./story-card";
import type { StoryForCreationDto } from "@/types";

type Props = {
  stories: StoryForCreationDto[];
  value?: string;
  onChange?: (storyId: string | undefined) => void;
};

const StoryPicker = ({ stories, value, onChange }: Props) => {
  return (
    <Carousel className="w-full md:w-[90%] self-center">
      <CarouselContent>
        {stories.map((s) => (
          <CarouselItem
            key={s.story.uuid}
            className="basis-full md:basis-1/2 xl:basis-1/3"
          >
            <button
              type="button"
              className={`transition-all cursor-pointer ${
                value === s.story.uuid ? "opacity-100 scale-100" : "opacity-40 scale-95"
              } hover:opacity-100`}
              onClick={() => onChange?.(s.story.uuid)}
            >
              <StoryCard summary={s.story} minPlayerCount={s.minPlayerCount} maxPlayerCount={s.maxPlayerCount}/>
            </button>
          </CarouselItem>
        ))}
      </CarouselContent>
      <CarouselPrevious className="hidden md:flex" />
      <CarouselNext className="hidden md:flex" />
    </Carousel>
  );
};

export default StoryPicker;
