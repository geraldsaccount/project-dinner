import * as React from "react";
import * as TooltipPrimitive from "@radix-ui/react-tooltip";

import { cn } from "@/lib/utils";

function TooltipProvider({
  delayDuration = 0,
  ...props
}: React.ComponentProps<typeof TooltipPrimitive.Provider>) {
  return (
    <TooltipPrimitive.Provider
      data-slot="tooltip-provider"
      delayDuration={delayDuration} // Keeping delayDuration as it's a functional choice
      {...props}
    />
  );
}

function Tooltip({
  ...props
}: React.ComponentProps<typeof TooltipPrimitive.Root>) {
  return (
    <TooltipProvider>
      <TooltipPrimitive.Root data-slot="tooltip" {...props} />
    </TooltipProvider>
  );
}

function TooltipTrigger({
  ...props
}: React.ComponentProps<typeof TooltipPrimitive.Trigger>) {
  return <TooltipPrimitive.Trigger data-slot="tooltip-trigger" {...props} />;
}

function TooltipContent({
  className,
  sideOffset = 0,
  children,
  ...props
}: React.ComponentProps<typeof TooltipPrimitive.Content>) {
  return (
    <TooltipPrimitive.Portal>
      <TooltipPrimitive.Content
        data-slot="tooltip-content"
        sideOffset={sideOffset}
        className={cn(
          // Brutalist changes for TooltipContent:
          // - bg-primary text-primary-foreground (already present, good for contrast)
          // - Removed rounded-md for sharp corners
          // - Added strong border (border-2 border-border)
          // - Removed all animations for immediate appearance/disappearance
          "bg-primary text-primary-foreground z-50 w-fit origin-(--radix-tooltip-content-transform-origin) rounded-none border-2 border-border px-3 py-1.5 text-xs text-balance", // Brutalist styling
          "data-[state=open]:animate-none data-[state=closed]:animate-none", // Remove all animations (fade, zoom, slide)
          className
        )}
        {...props}
      >
        {children}
        {/* Brutalist Arrow: sharp corners, solid fill, matching colors */}
        <TooltipPrimitive.Arrow className="bg-primary fill-primary z-50 size-3 translate-y-[calc(-50%_-_2px)] rotate-45 rounded-none border-2 border-primary" />{" "}
        {/* Brutalist arrow: larger size, no rounding, border */}
      </TooltipPrimitive.Content>
    </TooltipPrimitive.Portal>
  );
}

export { Tooltip, TooltipTrigger, TooltipContent, TooltipProvider };
