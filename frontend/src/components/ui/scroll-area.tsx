// imported from shadcn/ui

import * as React from "react";
import * as ScrollAreaPrimitive from "@radix-ui/react-scroll-area";

import { cn } from "@/lib/utils"; // Assuming cn is available or a simplified version

function ScrollArea({
  className,
  children,
  ...props
}: React.ComponentProps<typeof ScrollAreaPrimitive.Root>) {
  return (
    <ScrollAreaPrimitive.Root
      data-slot="scroll-area"
      // Brutalist changes for ScrollArea Root:
      // - Added a strong border around the entire scroll area
      // - Removed any inherent rounding (though it's usually applied to the parent)
      className={cn("relative border-2 border-border rounded-none", className)} // Brutalist styling
      {...props}
    >
      <ScrollAreaPrimitive.Viewport
        data-slot="scroll-area-viewport"
        // Brutalist changes for Viewport:
        // - Removed rounded-[inherit] for sharp corners
        // - Replaced subtle ring with stark outline on focus
        // - Removed transition for abrupt changes
        className="focus-visible:outline-2 focus-visible:outline-offset-0 focus-visible:outline-ring size-full rounded-none transition-none outline-none" // Brutalist styling
      >
        {children}
      </ScrollAreaPrimitive.Viewport>
      {/* Corner color updated for inverted scrollbar */}
      <ScrollBar />
      <ScrollAreaPrimitive.Corner className="bg-primary text-primary-foreground border-t-2 border-l-2 border-border rounded-none" />{" "}
      {/* Brutalist corner */}
    </ScrollAreaPrimitive.Root>
  );
}

function ScrollBar({
  className,
  orientation = "vertical",
  ...props
}: React.ComponentProps<typeof ScrollAreaPrimitive.ScrollAreaScrollbar>) {
  return (
    <ScrollAreaPrimitive.ScrollAreaScrollbar
      data-slot="scroll-area-scrollbar"
      orientation={orientation}
      // Brutalist changes for ScrollBar:
      // - bg-primary-foreground for the track (inverted color)
      // - Thinner width/height (w-2.5/h-2.5)
      // - Removed p-px for a flush look
      // - Removed transition-colors for abrupt changes
      // - Added strong borders (border-2 border-border)
      className={cn(
        "flex touch-none bg-primary-foreground border-2 border-border transition-none rounded-none", // Brutalist styling, inverted track color
        orientation === "vertical" && "h-full w-2.5 border-l-0 border-r-0", // Thinner vertical scrollbar
        orientation === "horizontal" && "h-2.5 flex-col border-t-0 border-b-0", // Thinner horizontal scrollbar
        className
      )}
      {...props}
    >
      <ScrollAreaPrimitive.ScrollAreaThumb
        data-slot="scroll-area-thumb"
        // Brutalist changes for ScrollAreaThumb:
        // - bg-primary for high contrast thumb (inverted color)
        // - Removed rounded-full for sharp, rectangular thumb
        // - Added border-2 border-primary to emphasize the thumb
        className="bg-primary relative flex-1 rounded-none border-2 border-primary" // Brutalist styling, inverted thumb color
      />
    </ScrollAreaPrimitive.ScrollAreaScrollbar>
  );
}

export { ScrollArea, ScrollBar };
