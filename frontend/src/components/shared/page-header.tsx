type Props = {
  title: string;
};

const PageHeader = ({ title }: Props) => {
  return <h2 className="text-5xl font-extrabold">{title}</h2>;
};

export default PageHeader;
