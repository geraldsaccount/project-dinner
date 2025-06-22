import { cn } from "@/lib/utils";
import React from "react"; // Explicitly import React

type Props = {
  title: string;
  className?: string;
};

const SectionHeader = ({ title, className }: Props) => {
  return (
    <h3
      // Brutalist styling for SectionHeader:
      // - text-foreground for high contrast
      // - font-bold and uppercase for strong typography
      // - Added a strong bottom border to define the section header, similar to cards/page header
      // - Increased text size for more prominence
      className={cn(
        "text-foreground text-2xl md:text-3xl font-bold uppercase pb-2 border-b-2 border-border border-primary mb-4", // Brutalist styling
        className
      )}
    >
      {title}
    </h3>
  );
};

export default SectionHeader;
