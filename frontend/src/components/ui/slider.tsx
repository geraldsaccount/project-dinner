// imported from shadcn/ui

import * as React from "react";
import * as SliderPrimitive from "@radix-ui/react-slider";

import { cn } from "@/lib/utils";

function Slider({
  className,
  defaultValue,
  value,
  min = 0,
  max = 100,
  ...props
}: React.ComponentProps<typeof SliderPrimitive.Root>) {
  const _values = React.useMemo(
    () =>
      Array.isArray(value)
        ? value
        : Array.isArray(defaultValue)
          ? defaultValue
          : [min, max],
    [value, defaultValue, min, max]
  );

  return (
    <SliderPrimitive.Root
      data-slot="slider"
      defaultValue={defaultValue}
      value={value}
      min={min}
      max={max}
      className={cn(
        // Brutalist changes for Slider Root:
        // - Ensure no rounded corners are forced by the root
        "relative flex w-full touch-none items-center select-none data-[disabled]:opacity-50 data-[orientation=vertical]:h-full data-[orientation=vertical]:min-h-44 data-[orientation=vertical]:w-auto data-[orientation=vertical]:flex-col",
        className
      )}
      {...props}
    >
      <SliderPrimitive.Track
        data-slot="slider-track"
        className={cn(
          // Brutalist changes for Slider Track:
          // - bg-muted (or background) for the track
          // - Removed rounded-full for sharp rectangular track
          // - Strong border around the track
          "bg-muted relative grow overflow-hidden rounded-none border-2 border-primary", // Brutalist styling
          "data-[orientation=horizontal]:h-2 data-[orientation=horizontal]:w-full", // Slightly thicker horizontal track
          "data-[orientation=vertical]:h-full data-[orientation=vertical]:w-2" // Slightly thicker vertical track
        )}
      >
        <SliderPrimitive.Range
          data-slot="slider-range"
          className={cn(
            // Brutalist changes for Slider Range:
            // - bg-primary for the filled range
            // - Ensure no rounding is applied
            "bg-primary absolute rounded-none", // Brutalist styling
            "data-[orientation=horizontal]:h-full data-[orientation=vertical]:w-full"
          )}
        />
      </SliderPrimitive.Track>
      {Array.from({ length: _values.length }, (_, index) => (
        <SliderPrimitive.Thumb
          data-slot="slider-thumb"
          key={index}
          className={cn(
            // Brutalist changes for Slider Thumb:
            // - border-primary bg-background for high contrast
            // - size-5 (slightly larger square)
            // - Removed rounded-full for sharp square
            // - Removed shadow-sm
            // - Replaced soft hover/focus ring with stark outline
            // - Removed transition
            "border-primary bg-background block size-5 shrink-0 rounded-none border-2 transition-none", // Brutalist styling
            "hover:outline-2 hover:outline-offset-0 hover:outline-ring", // Stark hover outline
            "focus-visible:outline-2 focus-visible:outline-offset-0 focus-visible:outline-ring", // Stark focus outline
            "disabled:pointer-events-none disabled:opacity-50" // Standard disabled
          )}
        />
      ))}
    </SliderPrimitive.Root>
  );
}

export { Slider };
