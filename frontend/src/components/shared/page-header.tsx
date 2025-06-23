import { cn } from "@/lib/utils";

type Props = {
  title: string;
  className?: string;
};

const PageHeader = ({ title, className }: Props) => {
  return (
    <h2
      className={cn(
        "text-4xl md:text-6xl lg:text-7xl font-extrabold uppercase leading-tight", // Brutalist typography
        // Text color is inherited from the outer div (text-primary-foreground)
        className
      )}
    >
      {title}
    </h2>
  );
};

export default PageHeader;
