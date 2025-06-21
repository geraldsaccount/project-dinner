import * as React from "react";
import * as SelectPrimitive from "@radix-ui/react-select";
import { CheckIcon, ChevronDownIcon, ChevronUpIcon } from "lucide-react";

import { cn } from "@/lib/utils";

function Select({
  ...props
}: React.ComponentProps<typeof SelectPrimitive.Root>) {
  return <SelectPrimitive.Root data-slot="select" {...props} />;
}

function SelectGroup({
  ...props
}: React.ComponentProps<typeof SelectPrimitive.Group>) {
  return <SelectPrimitive.Group data-slot="select-group" {...props} />;
}

function SelectValue({
  ...props
}: React.ComponentProps<typeof SelectPrimitive.Value>) {
  return <SelectPrimitive.Value data-slot="select-value" {...props} />;
}

function SelectTrigger({
  className,
  size = "default",
  children,
  ...props
}: React.ComponentProps<typeof SelectPrimitive.Trigger> & {
  size?: "sm" | "default";
}) {
  return (
    <SelectPrimitive.Trigger
      data-slot="select-trigger"
      data-size={size}
      className={cn(
        // Brutalist changes for SelectTrigger:
        // - Removed rounded-md for sharp corners
        // - Replaced border-input with border-2 border-border
        // - Removed shadow-xs
        // - Replaced subtle focus ring with stark outline
        // - Removed transition
        // - bg-background text-foreground for high contrast
        "data-[placeholder]:text-muted-foreground", // Placeholder color
        "[&_svg:not([class*='text-'])]:text-foreground", // Chevron icon color (text-foreground)
        "bg-background text-foreground flex w-fit items-center justify-between gap-2 rounded-none border-2 border-border px-3 py-2 text-sm whitespace-nowrap transition-none outline-none", // Brutalist base styling
        "focus-visible:outline-2 focus-visible:outline-offset-0 focus-visible:outline-ring", // Stark focus outline
        "aria-invalid:border-destructive aria-invalid:outline-destructive", // Error state: destructive border/outline
        "disabled:cursor-not-allowed disabled:opacity-50", // Standard disabled state
        "data-[size=default]:h-10 data-[size=sm]:h-8", // Adjusted height for brutalist feel
        "*:data-[slot=select-value]:line-clamp-1 *:data-[slot=select-value]:flex *:data-[slot=select-value]:items-center *:data-[slot=select-value]:gap-2",
        "[&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4",
        className
      )}
      {...props}
    >
      {children}
      <SelectPrimitive.Icon asChild>
        {/* Chevron icon explicitly uses text-foreground for visibility */}
        <ChevronDownIcon className="size-4 text-foreground" />
      </SelectPrimitive.Icon>
    </SelectPrimitive.Trigger>
  );
}

function SelectContent({
  className,
  children,
  position = "popper",
  ...props
}: React.ComponentProps<typeof SelectPrimitive.Content>) {
  return (
    <SelectPrimitive.Portal>
      <SelectPrimitive.Content
        data-slot="select-content"
        className={cn(
          // Brutalist changes for SelectContent:
          // - bg-background text-foreground
          // - Removed rounded-md
          // - Removed shadow-md
          // - Strong border-2 border-border
          // - Removed all transition animations
          "bg-background text-foreground relative z-50 max-h-(--radix-select-content-available-height) min-w-[8rem] origin-(--radix-select-content-transform-origin) overflow-x-hidden overflow-y-auto rounded-none border-2 border-border", // Brutalist base styling
          "data-[state=open]:animate-none data-[state=closed]:animate-none", // Removed all animations
          position === "popper" &&
            "data-[side=bottom]:translate-y-1 data-[side=left]:-translate-x-1 data-[side=right]:translate-x-1 data-[side=top]:-translate-y-1", // Positioning (keep subtle offset)
          className
        )}
        position={position}
        {...props}
      >
        <SelectScrollUpButton />
        <SelectPrimitive.Viewport
          className={cn(
            "p-1", // Keep internal padding
            position === "popper" &&
              "h-[var(--radix-select-trigger-height)] w-full min-w-[var(--radix-select-trigger-width)] scroll-my-1"
          )}
        >
          {children}
        </SelectPrimitive.Viewport>
        <SelectScrollDownButton />
      </SelectPrimitive.Content>
    </SelectPrimitive.Portal>
  );
}

function SelectLabel({
  className,
  ...props
}: React.ComponentProps<typeof SelectPrimitive.Label>) {
  return (
    <SelectPrimitive.Label
      data-slot="select-label"
      // Brutalist changes for SelectLabel:
      // - font-bold uppercase
      // - text-foreground for high contrast
      className={cn(
        "text-foreground px-2 py-1.5 text-xs font-bold uppercase",
        className
      )} // Brutalist styling
      {...props}
    />
  );
}

function SelectItem({
  className,
  children,
  ...props
}: React.ComponentProps<typeof SelectPrimitive.Item>) {
  return (
    <SelectPrimitive.Item
      data-slot="select-item"
      className={cn(
        // Brutalist changes for SelectItem:
        // - Removed rounded-sm
        // - Focus state: bg-accent text-accent-foreground
        // - No subtle effects, direct selection
        "focus:bg-accent focus:text-accent-foreground", // Focus state
        "[&_svg:not([class*='text-'])]:text-foreground", // Checkmark icon color
        "relative flex w-full cursor-default items-center gap-2 rounded-none py-1.5 pr-8 pl-2 text-sm outline-none select-none transition-none", // Brutalist base styling
        "data-[disabled]:pointer-events-none data-[disabled]:opacity-50", // Disabled state
        "[&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4",
        "*:data-[slot=select-value]:line-clamp-1 *:data-[slot=select-value]:flex *:data-[slot=select-value]:items-center *:data-[slot=select-value]:gap-2",
        className
      )}
      {...props}
    >
      <span className="absolute right-2 flex size-3.5 items-center justify-center">
        <SelectPrimitive.ItemIndicator>
          {/* Checkmark icon explicitly uses text-foreground */}
          <CheckIcon className="size-4 text-primary" />{" "}
          {/* Changed to primary for strong mark */}
        </SelectPrimitive.ItemIndicator>
      </span>
      <SelectPrimitive.ItemText>{children}</SelectPrimitive.ItemText>
    </SelectPrimitive.Item>
  );
}

function SelectSeparator({
  className,
  ...props
}: React.ComponentProps<typeof SelectPrimitive.Separator>) {
  return (
    <SelectPrimitive.Separator
      data-slot="select-separator"
      // Brutalist SelectSeparator: bg-border, h-px (kept as is, already brutalist)
      className={cn("bg-border pointer-events-none -mx-1 my-1 h-px", className)}
      {...props}
    />
  );
}

function SelectScrollUpButton({
  className,
  ...props
}: React.ComponentProps<typeof SelectPrimitive.ScrollUpButton>) {
  return (
    <SelectPrimitive.ScrollUpButton
      data-slot="select-scroll-up-button"
      // Brutalist Scroll Buttons: Use bg-accent, text-accent-foreground
      className={cn(
        "flex cursor-default items-center justify-center py-1 bg-accent text-accent-foreground border-b-2 border-border rounded-none", // Brutalist styling
        className
      )}
      {...props}
    >
      <ChevronUpIcon className="size-4" />{" "}
      {/* Icon inherits color from parent */}
    </SelectPrimitive.ScrollUpButton>
  );
}

function SelectScrollDownButton({
  className,
  ...props
}: React.ComponentProps<typeof SelectPrimitive.ScrollDownButton>) {
  return (
    <SelectPrimitive.ScrollDownButton
      data-slot="select-scroll-down-button"
      // Brutalist Scroll Buttons: Use bg-accent, text-accent-foreground
      className={cn(
        "flex cursor-default items-center justify-center py-1 bg-accent text-accent-foreground border-t-2 border-border rounded-none", // Brutalist styling
        className
      )}
      {...props}
    >
      <ChevronDownIcon className="size-4" />{" "}
      {/* Icon inherits color from parent */}
    </SelectPrimitive.ScrollDownButton>
  );
}

export {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectScrollDownButton,
  SelectScrollUpButton,
  SelectSeparator,
  SelectTrigger,
  SelectValue,
};
