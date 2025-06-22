// imported from shadcn/ui

import * as React from "react";
import useEmblaCarousel, {
  type UseEmblaCarouselType,
} from "embla-carousel-react";
import { ArrowLeft, ArrowRight } from "lucide-react";

import { cn } from "@/lib/utils";
// Assuming Button component from your brutalist button.tsx is available
import { Button } from "@/components/ui/button";

type CarouselApi = UseEmblaCarouselType[1];
type UseCarouselParameters = Parameters<typeof useEmblaCarousel>;
type CarouselOptions = UseCarouselParameters[0];
type CarouselPlugin = UseCarouselParameters[1];

type CarouselProps = {
  opts?: CarouselOptions;
  plugins?: CarouselPlugin;
  orientation?: "horizontal" | "vertical";
  setApi?: (api: CarouselApi) => void;
};

type CarouselContextProps = {
  carouselRef: ReturnType<typeof useEmblaCarousel>[0];
  api: ReturnType<typeof useEmblaCarousel>[1];
  scrollPrev: () => void;
  scrollNext: () => void;
  canScrollPrev: boolean;
  canScrollNext: boolean;
} & CarouselProps;

const CarouselContext = React.createContext<CarouselContextProps | null>(null);

function useCarousel() {
  const context = React.useContext(CarouselContext);

  if (!context) {
    throw new Error("useCarousel must be used within a <Carousel />");
  }

  return context;
}

function Carousel({
  orientation = "horizontal",
  opts,
  setApi,
  plugins,
  className,
  children,
  ...props
}: React.ComponentProps<"div"> & CarouselProps) {
  const [carouselRef, api] = useEmblaCarousel(
    {
      ...opts,
      axis: orientation === "horizontal" ? "x" : "y",
    },
    plugins
  );
  const [canScrollPrev, setCanScrollPrev] = React.useState(false);
  const [canScrollNext, setCanScrollNext] = React.useState(false);

  const onSelect = React.useCallback((api: CarouselApi) => {
    if (!api) return;
    setCanScrollPrev(api.canScrollPrev());
    setCanScrollNext(api.canScrollNext());
  }, []);

  const scrollPrev = React.useCallback(() => {
    api?.scrollPrev();
  }, [api]);

  const scrollNext = React.useCallback(() => {
    api?.scrollNext();
  }, [api]);

  const handleKeyDown = React.useCallback(
    (event: React.KeyboardEvent<HTMLDivElement>) => {
      if (event.key === "ArrowLeft") {
        event.preventDefault();
        scrollPrev();
      } else if (event.key === "ArrowRight") {
        event.preventDefault();
        scrollNext();
      }
    },
    [scrollPrev, scrollNext]
  );

  React.useEffect(() => {
    if (!api || !setApi) return;
    setApi(api);
  }, [api, setApi]);

  React.useEffect(() => {
    if (!api) return;
    onSelect(api);
    api.on("reInit", onSelect);
    api.on("select", onSelect);

    return () => {
      api?.off("select", onSelect);
    };
  }, [api, onSelect]);

  return (
    <CarouselContext.Provider
      value={{
        carouselRef,
        api: api,
        opts,
        orientation:
          orientation || (opts?.axis === "y" ? "vertical" : "horizontal"),
        scrollPrev,
        scrollNext,
        canScrollPrev,
        canScrollNext,
      }}
    >
      <div
        onKeyDownCapture={handleKeyDown}
        data-slot="carousel"
        // Brutalist Carousel Root:
        // - Strong border around the entire carousel
        // - bg-background for content area
        className={cn(
          "relative border-primary border-x-2 bg-background",
          className
        )} // Brutalist styling
        role="region"
        aria-roledescription="carousel"
        {...props}
      >
        {children}
      </div>
    </CarouselContext.Provider>
  );
}

function CarouselContent({ className, ...props }: React.ComponentProps<"div">) {
  const { carouselRef, orientation } = useCarousel();

  return (
    <div
      ref={carouselRef}
      data-slot="carousel-content"
      // Brutalist Carousel Content:
      // - Removed overflow-hidden to allow for potential visual glitches if items aren't perfect (brutalist vibe)
      // - Added internal padding if desired for content spacing
      className="p-4 overflow-clip" // Added general padding to the content area
    >
      <div
        className={cn(
          "flex",
          // Removed negative margins, content will fill carousel based on CarouselItem padding
          orientation === "horizontal" ? "" : "flex-col",
          className
        )}
        {...props}
      />
    </div>
  );
}

function CarouselItem({ className, ...props }: React.ComponentProps<"div">) {
  const { orientation } = useCarousel();

  return (
    <div
      role="group"
      aria-roledescription="slide"
      data-slot="carousel-item"
      // Brutalist Carousel Item:
      // - Strong border around each item
      // - bg-card for distinct item background
      // - No rounding
      className={cn(
        "min-w-0 shrink-0 grow-0 basis-full rounded-none", // Brutalist styling
        // Re-introduced padding here instead of negative margin on content
        orientation === "horizontal" ? "pr-4" : "pb-4", // Use padding on item itself
        className
      )}
      {...props}
    />
  );
}

function CarouselPrevious({
  className,
  variant = "outline", // Use brutalist outline variant for the button
  size = "icon", // Use brutalist icon size for the button
  ...props
}: React.ComponentProps<typeof Button>) {
  const { orientation, scrollPrev, canScrollPrev } = useCarousel();

  return (
    <Button
      data-slot="carousel-previous"
      variant={variant}
      size={size}
      className={cn(
        // Brutalist Previous Button:
        // - Removed rounded-full for sharp square button
        // - Adjusted positioning for a more explicit, architectural feel
        "absolute size-10 bg-transparent border-none", // Strong border, sharp corners, larger size
        orientation === "horizontal"
          ? "top-1/2 -left-8 -translate-y-1/2" // Adjusted left position for closer placement
          : "-top-8 left-1/2 -translate-x-1/2 rotate-90", // Adjusted top position
        className
      )}
      disabled={!canScrollPrev}
      onClick={scrollPrev}
      {...props}
    >
      <ArrowLeft />
      <span className="sr-only">Previous slide</span>
    </Button>
  );
}

function CarouselNext({
  className,
  variant = "outline", // Use brutalist outline variant for the button
  size = "icon", // Use brutalist icon size for the button
  ...props
}: React.ComponentProps<typeof Button>) {
  const { orientation, scrollNext, canScrollNext } = useCarousel();

  return (
    <Button
      data-slot="carousel-next"
      variant={variant}
      size={size}
      className={cn(
        // Brutalist Next Button:
        // - Removed rounded-full for sharp square button
        // - Adjusted positioning for a more explicit, architectural feel
        "absolute bg-transparent border-none size-10", // Strong border, sharp corners, larger size
        orientation === "horizontal"
          ? "top-1/2 -right-8 -translate-y-1/2" // Adjusted right position for closer placement
          : "-bottom-8 left-1/2 -translate-x-1/2 rotate-90", // Adjusted bottom position
        className
      )}
      disabled={!canScrollNext}
      onClick={scrollNext}
      {...props}
    >
      <ArrowRight />
      <span className="sr-only">Next slide</span>
    </Button>
  );
}

export {
  type CarouselApi,
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselPrevious,
  CarouselNext,
};
